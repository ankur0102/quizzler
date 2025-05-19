import React, { useEffect, useState } from "react";
import axios from "../services/api";
import { subscribeToSSE } from "../services/sse";
import { useLocation, useNavigate } from "react-router-dom";
import { Button, Card, Col, Container, Form, Row } from "react-bootstrap";
import "./css/styles.css";
import ResultBoard from "./composed-components/ResultBoard";
import QuizInProgress from "./composed-components/QuizInProgress";
import Question from "./composed-components/Question";
import WaitForHost from "./composed-components/WaitForHost";
import CountdownTimer from "./composed-components/CountdownTimer";

const QuizPlayArea = () => {
    const [question, setQuestion] = useState(null);
    const [answer, setAnswer] = useState("");
    const [leaderboard, setLeaderboard] = useState({ playerScores: [] });
    const [submitText, setSubmitText] = useState("Submit Answer");
    const [disableSubmit, setDisableSubmit] = useState(false);
    const location = useLocation();
    const navigate = useNavigate();

    const { state } = location || {};
    const { sessionId } = state || {};

    const fetchLeaderboard = async () => {
        try {
            const response = await axios.get(`/sessions/${sessionId}/leaderboard`);
            setLeaderboard(response.data); // Update leaderboard state
        } catch (error) {
            console.error("Error fetching leaderboard:", error);
        }
    };

    // Subscribe to websocket events
    useEffect(() => {
        const unsubscribe = subscribeToSSE(sessionId, state, async (data) => {
            if (data.redirectToLeaderBoard === true) {
                navigate("/leaderboard", { state: { sessionId: sessionId } });
            } else {
                setQuestion({
                    ...data.question,
                    endTime: data.endTime,
                    currentIndex: data.currentIndex,
                    totalQuestions: data.totalQuestions,
                });
                setAnswer("");
                setSubmitText("Submit Answer");
                setDisableSubmit(false);
                await fetchLeaderboard();
            }
        });

        return () => unsubscribe();
    }, [sessionId, state]);

    const submitAnswer = async () => {
        try {
            setSubmitText("Submitting..."); // Disable the button
            setDisableSubmit(true); // Disable the button
            const { playerId, sessionId } = state;
            const submittedAt = Date.now();
            const requestData = {
                playerId,
                questionId: question.id,
                sessionId,
                answer,
                submittedAt,
            };
            console.log(requestData);

            const response = await axios.post(`/sessions/${sessionId}/answer`, requestData);
            console.log("Current Question is ", question);
            if (response.status === 200) {
                setSubmitText("Submitted!");
            }
        } catch (error) {
            if (error.response?.data?.errorCode) {
                console.log(error.response.data.errorCode);
                setSubmitText("Time Up");
            } else {
                console.error("Error submitting answer:", error);
            }
        }
    };

    const onTimeUp = () => {
        setDisableSubmit(true);
    }

    if (!question) return <WaitForHost />;

    return (
        <Container className="my-4">
            <QuizInProgress />
            <Row className="justify-content-center">
                <Col md={6} className="mb-4">
                    <Card style={{ width: "100%" }} className="shadow-sm">
                        <Question question={question} />
                        <Card.Body>
                            <Form>
                                {question.options.map((option, index) => (
                                    <Form.Group
                                        key={index}
                                        className="mb-3"
                                        controlId={`${question.id}-${index}`}
                                    >
                                        <Form.Check
                                            type="radio"
                                            label={option}
                                            name="answer"
                                            value={option}
                                            checked={answer === option}
                                            onChange={(e) => setAnswer(e.target.value)}
                                        />
                                    </Form.Group>
                                ))}
                            </Form>
                        </Card.Body>
                        <Card.Footer className="d-flex justify-content-between align-items-center">
                            <CountdownTimer endTime={question.endTime} submitText={submitText} />
                            <Button
                                variant="success"
                                onClick={submitAnswer}
                                disabled={disableSubmit || !answer}
                            >
                                {submitText}
                            </Button>
                        </Card.Footer>
                    </Card>
                </Col>
                <ResultBoard leaderboard={leaderboard} />
            </Row>
        </Container>
    );
};

export default QuizPlayArea;
