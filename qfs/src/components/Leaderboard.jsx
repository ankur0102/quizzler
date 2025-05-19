import React, { useEffect, useState } from "react";
import axios from "../services/api";
import { useLocation } from "react-router-dom";
import { Card, Table, Container } from "react-bootstrap";

const Leaderboard = () => {
    const [leaderboard, setLeaderboard] = useState({ playerScores: [] });
    const location = useLocation();
    const { state } = location || {};
    const { sessionId } = state || {};

    useEffect(() => {
        const fetchLeaderboard = async () => {
            try {
                const response = await axios.get(`/sessions/${sessionId}/leaderboard`);
                setLeaderboard(response.data); // Update leaderboard state
            } catch (error) {
                console.error("Error fetching leaderboard:", error);
            }
        };

        fetchLeaderboard();
    }, [sessionId]);

    return (
        <Container className="my-4">
            <Card className="shadow-sm">
                <Card.Header className="bg-success text-white text-center">
                    Leaderboard
                </Card.Header>
                <Card.Body>
                    <Table striped bordered hover size="sm">
                        <thead>
                        <tr>
                            <th>Rank</th>
                            <th>Player</th>
                            <th>Score</th>
                        </tr>
                        </thead>
                        <tbody>
                        {leaderboard.playerScores.map((player, index) => (
                            <tr key={index}>
                                <td>{index + 1}</td>
                                <td>{player.playerName}</td>
                                <td>{player.score}</td>
                            </tr>
                        ))}
                        </tbody>
                    </Table>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default Leaderboard;