import {Card} from "react-bootstrap";
import React from "react";

const QuizInProgress = () => (
    <Card className="mb-4 shadow-sm">
        <Card.Header className="bg-warning text-dark text-center d-flex justify-content-center align-items-center">
            <div className="blinking-dot"></div>
            <span className="ms-2">Quiz In Progress</span>
        </Card.Header>
    </Card>
);

export default QuizInProgress;