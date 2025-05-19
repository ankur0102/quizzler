import React, { useState } from "react";
import axios from "../services/api";
import {useLocation, useNavigate} from "react-router-dom";
import {Button, Card, Container, Form} from "react-bootstrap";

const PlayerLobby = ({ onPlayerJoined, sessionId }) => {
    const [roomId, setRoomId] = useState("");
    const [nickname, setNickname] = useState("");
    const navigate = useNavigate();

    const joinQuiz = async () => {
        try {
            const response = await axios.post(`/sessions/${roomId}/join`, { nickname });
            if (response.status === 200) {
                onPlayerJoined(response.data);
                const websocketEP = response.data.websocketEP;
                const { playerId, sessionId } = response.data;
                navigate("/play", { state: { playerId, sessionId, websocketEP, host: false } });
            }
        } catch (error) {
            console.error("Error joining quiz:", error);
        }
    };

    return (
        <Container className="my-4 d-flex justify-content-center">
            <Card style={{ width: "24rem" }} className="shadow-sm">
                <Card.Header className="bg-success text-white text-center">
                    Join Quiz
                </Card.Header>
                <Card.Body>
                    <Card.Text className="text-center mb-4">
                        Please enter a nickname and session ID to join
                    </Card.Text>
                    <Form>
                        {/* Room ID Input */}
                        <Form.Group className="mb-3" controlId="formRoomId">
                            <Form.Label>Room ID</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Enter Room ID"
                                value={roomId}
                                onChange={(e) => setRoomId(e.target.value)}
                            />
                        </Form.Group>
                        <Form.Group className="mb-4" controlId="formNickname">
                            <Form.Label>Nickname</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Enter Nickname"
                                value={nickname}
                                onChange={(e) => setNickname(e.target.value)}
                            />
                        </Form.Group>
                        <div className="d-flex justify-content-center">
                            <Button variant="primary" onClick={joinQuiz}>
                                Join Quiz
                            </Button>
                        </div>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default PlayerLobby;