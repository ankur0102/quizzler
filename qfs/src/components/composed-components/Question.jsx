import {Card} from "react-bootstrap";
import React from "react";

const Question = ({ question }) => (
<Card.Header className="bg-primary text-white">
    Q{question.currentIndex + 1}. {question.text}
</Card.Header>
);

export default Question;