import React, {useEffect, useState} from "react";
import axios from "../services/api";
import {subscribeToSSE} from "../services/sse";
import {useLocation, useNavigate} from "react-router-dom";
import {Button, Card, Col, Container, ListGroup, Row} from "react-bootstrap";
import ResultBoard from "./composed-components/ResultBoard";
import "./css/styles.css";
import QuizInProgress from "./composed-components/QuizInProgress";
import Question from "./composed-components/Question";
import QuizFooterHost from "./QuizFooterHost";
import WaitForHost from "./composed-components/WaitForHost";

const HostQuizPanel = () => {
    const [question, setQuestion] = useState(null);
    const [leaderboard, setLeaderboard] = useState({playerScores: []});
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [resultButton, setResultButton] = useState(false);

    const location = useLocation();
    const navigate = useNavigate();

    const { state } = location || {};
    const { sessionId } = state || {};

    const fetchLeaderboard = async () => {
        try {
            const response = await axios.get(`/sessions/${sessionId}/leaderboard`);
            setLeaderboard(response.data);
        } catch (error) {
            console.error("Error fetching leaderboard:", error);
        }
    };

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

                if (data.currentIndex + 1 === data.totalQuestions) {
                    setResultButton(true);
                } else {
                    setIsSubmitting(false);
                }
                await fetchLeaderboard();
            }
        });

        return () => unsubscribe();
    }, [sessionId]);

    const nextQuestion = async () => {
        try {
            setIsSubmitting(true);
            const { playerId, sessionId } = state;

            await axios.post(`/sessions/${sessionId}/next`, {playerId}).catch((error) => {
                if (error.response) {
                    console.error("Error Response:", error.response.data);
                } else if (error.request) {
                    console.error("No Response Received:", error.request);
                } else {
                    console.error("Error Message:", error.message);
                }
            });
        } catch (error) {
            console.error("Error submitting answer:", error);
        }
    };

    if (!question) return <WaitForHost />;

    return (
        <Container className="my-4">
            <QuizInProgress/>
            <Row className="justify-content-center">
                <Col md={6} className="mb-4">
                    <Card style={{width: "100%"}} className="shadow-sm">
                        <Question question={question}/>
                        <Card.Body>
                            <ListGroup variant="flush">
                                {question.options.map((option, index) => (
                                    <ListGroup.Item key={index}>
                                        <strong>Option {index + 1}:</strong> {option}
                                    </ListGroup.Item>
                                ))}
                            </ListGroup>
                        </Card.Body>
                        <QuizFooterHost endTime={question.endTime}
                                        isLastQuestion={question.currentIndex + 1 === question.totalQuestions}
                                        onClickNext={nextQuestion}/>
                    </Card>
                </Col>
                <ResultBoard leaderboard={leaderboard}/>
            </Row>
        </Container>
    );
};

export default HostQuizPanel;