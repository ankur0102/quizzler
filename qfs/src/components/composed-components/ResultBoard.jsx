import {Card, Col, Table} from "react-bootstrap";
import React from "react";

const ResultBoard = (props) => {
    const { leaderboard } = props;
    return (
        <Col md={6} className="mb-4">
            <Card style={{ width: "100%" }} className="shadow-sm">
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
        </Col>
    )
};

export default ResultBoard;