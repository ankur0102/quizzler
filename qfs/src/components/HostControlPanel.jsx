import React, { useState } from "react";
import axios from "../services/api";
import { useNavigate } from "react-router-dom";
import { Card, Button, Form, Container } from "react-bootstrap";

const HostControlPanel = ({ onHostSessionCreated }) => {
    const navigate = useNavigate();

    const getAllQuiz = async () => {
        try {
            const response = await axios.post(`/sessions`, { nickname: "host" });
            if (response.status === 200) {
                onHostSessionCreated(response.data);
                navigate("/selectQuiz", { state: response.data });
            }
        } catch (error) {
            console.error("Error starting quiz:", error);
        }
    };

    return (
        <Container className="my-4 d-flex justify-content-center">
            <Card style={{ width: "24rem" }} className="shadow-sm">
                <Card.Header className="bg-primary text-white text-center">
                    Host Control Panel
                </Card.Header>
                <Card.Body>
                    <Card.Text className="text-center mb-4">
                        Please select an existing quiz to get started, or create a new one.
                    </Card.Text>
                    <div className="d-flex justify-content-center my-5">
                        <Card.Img
                            variant="top"
                            src="/nyancat-rainbow-cat.gif"
                            alt="Loading GIF"
                            style={{ width: "150px" }}
                        />
                    </div>
                    <Form className="mt-4">
                        <div className="d-flex justify-content-center gap-2">
                            <Button variant="success" onClick={() => navigate("/createQuiz")}>
                                Create Quiz
                            </Button>
                            <Button variant="primary" onClick={getAllQuiz}>
                                Start Quiz
                            </Button>
                        </div>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default HostControlPanel;
