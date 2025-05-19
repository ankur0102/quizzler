import React, { useState } from "react";
import axios from "../services/api";
import {useLocation, useNavigate} from "react-router-dom";
import { Card, Button, Form, Container, Row, Col, Tooltip, OverlayTrigger, Alert } from "react-bootstrap";
import { FaCopy, FaCheck } from "react-icons/fa"; // Import copy and check icons


const HostSelectQuiz = () => {
    const [quizTitle, setQuizTitle] = useState("");
    const [quizId, setQuizId] = useState(null);
    const [copied, setCopied] = useState(false); // State to track if copied
    const location = useLocation();
    const navigate = useNavigate();
    const { sessionId, playerId, quizzes } = location.state || {};

    const createQuiz = async () => {
        try {
            const response = await axios.post("/quizzes", { title: quizTitle });
            console.log(response);
            setQuizId(response.data.id);
        } catch (error) {
            console.error("Error creating quiz:", error);
        }
    };

    const startQuiz = async (quiz) => {
        try {
            navigate("/quizPanel", { state: {quiz, playerId, sessionId, host: true} });
        } catch (error) {
            console.error("Error starting quiz:", error);
        }
    };
    const handleCopy = () => {
        // Copy sessionId to clipboard
        navigator.clipboard.writeText(sessionId);
        setCopied(true);
        setTimeout(() => {
            setCopied(false);
        }, 2000);
    };

    return (
        <Container className="my-4">
            <Card className="shadow-sm">
                <Card.Body>
                    <Card.Title className="text-center mb-4">Host Control Panel</Card.Title>
                    <Card.Subtitle className="text-center text-muted mb-4 d-flex justify-content-center align-items-center">
                        <span>Session ID: {sessionId}</span>
                        <Button
                            variant="link"
                            className="ms-2 p-0 text-decoration-none"
                            onClick={handleCopy}
                        >
                            {copied ? (
                                <FaCheck size={18} className="text-success" />
                            ) : (
                                <FaCopy size={18} />
                            )}
                        </Button>
                    </Card.Subtitle>
                    <Card.Text className="mb-4">
                        <strong>Please choose from the available quizzes below to get started:</strong>
                    </Card.Text>
                    <div>
                        {quizzes.length > 0 ? (
                            quizzes.map((quiz, index) => (
                                <div key={quiz.id} className="d-flex justify-content-between align-items-center mb-2">
                                    <span>{index + 1}. {quiz.title}</span>
                                    <Button
                                        variant="primary"
                                        size="sm"
                                        onClick={() => startQuiz(quiz)}
                                    >
                                        Start Quiz
                                    </Button>
                                </div>
                            ))
                        ) : (
                            <p className="text-muted">No quizzes available. Create one below.</p>
                        )}
                    </div>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default HostSelectQuiz;