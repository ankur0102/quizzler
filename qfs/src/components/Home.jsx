import React from "react";
import { useNavigate } from "react-router-dom";
import { Container, Card, Button } from "react-bootstrap";

const Home = () => {
    const navigate = useNavigate();
    return (
        <Container className="d-flex justify-content-center align-items-center" style={{ height: "100vh" }}>
            <Card style={{ width: "28rem" }} className="text-center shadow-sm">
                <Card.Body>
                    <Card.Title className="mb-4" style={{ fontSize: "1.5rem", fontWeight: "bold" }}>
                        Welcome to the Quiz App
                    </Card.Title>
                    <Card.Text className="mb-4">
                        Please select an option to join as a host or a player.
                    </Card.Text>

                    <div className="d-flex flex-column gap-3">
                        <Button variant="primary" size="lg" onClick={() => navigate("/host")}>
                            Join as Host
                        </Button>
                        <Button variant="success" size="lg" onClick={() => navigate("/lobby")}>
                            Join as Player
                        </Button>
                    </div>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default Home;