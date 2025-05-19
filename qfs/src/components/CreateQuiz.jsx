import React, { useState } from "react";
import axios from "../services/api";
import { useNavigate } from "react-router-dom";
import { Container, Card, Form, Button } from "react-bootstrap";

const CreateQuiz = () => {
    const [quizTitle, setQuizTitle] = useState("");
    const [quizResponse, setQuizResponse] = useState(null);
    const [questions, setQuestions] = useState([]);
    const [currentQuestion, setCurrentQuestion] = useState({
        text: "",
        options: ["", "", "", ""],
        correctOption: null,
    });
    const navigate = useNavigate();

    const handleCreateQuiz = async () => {
        try {
            const response = await axios.post("/quizzes", { title: quizTitle });
            setQuizResponse(response.data);
            alert(`Quiz created successfully! Title: ${response.data.title}, ID: ${response.data.id}`);
        } catch (error) {
            console.error("Error creating quiz:", error);
            alert("Failed to create quiz. Please try again.");
        }
    };

    const handleAddQuestion = () => {
        if (
            !currentQuestion.text.trim() ||
            currentQuestion.correctOption === null ||
            currentQuestion.options.some((option) => !option.trim())
        ) {
            alert("Please fill in all fields and select the correct answer.");
            return;
        }

        setQuestions([...questions, currentQuestion]); // Add the current question to the list
        setCurrentQuestion({
            text: "",
            options: ["", "", "", ""],
            correctOption: null,
        });
    };

    const handleSubmitQuestions = async () => {
        if (questions.length === 0) {
            alert("No questions added. Please add at least one question.");
            return;
        }

        try {
            const response = await axios.post(`/quizzes/${quizResponse.id}/questions`, {
                questions: questions.map((q) => ({
                    text: q.text,
                    options: q.options,
                    correctOption: q.correctOption,
                })),
            });
            if (response.status === 200) {
                console.log("Questions submitted successfully!");
                navigate("/host");
            }
        } catch (error) {
            console.error("Error submitting questions:", error);
            alert("Failed to submit questions. Please try again.");
        }
    };

    return (
        <Container className="my-4 d-flex justify-content-center">
            <Card style={{ width: "28rem" }} className="shadow-sm">
                <Card.Header className="bg-primary text-white text-center">
                    Create a New Quiz
                </Card.Header>
                <Card.Body>
                    {!quizResponse && (
                        <>
                            <Card.Text className="text-center mb-4">
                                Please enter a title for your quiz.
                            </Card.Text>

                            <Form>
                                <Form.Group className="mb-3" controlId="formQuizTitle">
                                    <Form.Label>Quiz Title</Form.Label>
                                    <Form.Control
                                        type="text"
                                        placeholder="Enter Quiz Title"
                                        value={quizTitle}
                                        onChange={(e) => setQuizTitle(e.target.value)}
                                    />
                                </Form.Group>
                                <Button
                                    variant="success"
                                    disabled={!quizTitle.trim()}
                                    onClick={handleCreateQuiz}
                                >
                                    Create Quiz
                                </Button>
                            </Form>
                        </>
                    )}
                    {quizResponse && (
                        <>
                            <Card.Text className="text-center mb-4">
                                Quiz Created! Add questions below.
                            </Card.Text>
                            <Form>
                                <Form.Group className="mb-3" controlId="formQuestionText">
                                    <Form.Label>Question</Form.Label>
                                    <Form.Control
                                        type="text"
                                        placeholder="Enter question"
                                        value={currentQuestion.text}
                                        onChange={(e) =>
                                            setCurrentQuestion({
                                                ...currentQuestion,
                                                text: e.target.value,
                                            })
                                        }
                                    />
                                </Form.Group>
                                {currentQuestion.options.map((option, index) => (
                                    <Form.Group className="mb-3" key={index} controlId={`formOption${index}`}>
                                        <Form.Label>Option {index + 1}</Form.Label>
                                        <Form.Control
                                            type="text"
                                            placeholder={`Enter Option ${index + 1}`}
                                            value={option}
                                            onChange={(e) => {
                                                const updatedOptions = [...currentQuestion.options];
                                                updatedOptions[index] = e.target.value;
                                                setCurrentQuestion({
                                                    ...currentQuestion,
                                                    options: updatedOptions,
                                                });
                                            }}
                                        />
                                    </Form.Group>
                                ))}
                                <Form.Group className="mb-3" controlId="formCorrectOption">
                                    <Form.Label>Correct Answer</Form.Label>
                                    <Form.Select
                                        value={currentQuestion.correctOption || ""}
                                        onChange={(e) =>
                                            setCurrentQuestion({
                                                ...currentQuestion,
                                                correctOption: e.target.value,
                                            })
                                        }
                                    >
                                        <option value="" disabled>
                                            Select correct answer
                                        </option>
                                        {currentQuestion.options.map((option, index) => (
                                            <option key={index} value={option}>
                                                {option}
                                            </option>
                                        ))}
                                    </Form.Select>
                                </Form.Group>
                                <Button
                                    variant="primary"
                                    className="me-2"
                                    onClick={handleAddQuestion}
                                >
                                    Add Question
                                </Button>
                            </Form>
                            <div className="mt-4 text-center">
                                <Button
                                    variant="success"
                                    onClick={handleSubmitQuestions}
                                    disabled={questions.length === 0}
                                >
                                    Submit All Questions
                                </Button>
                            </div>
                        </>
                    )}
                </Card.Body>
            </Card>
        </Container>
    );
};

export default CreateQuiz;
