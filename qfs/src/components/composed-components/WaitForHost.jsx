import {Card, Container} from "react-bootstrap";
import React from "react";

const WaitForHost = () => (
    <Container className="d-flex justify-content-center align-items-center" style={{height: "100vh"}}>
        <Card style={{width: "28rem"}} className="text-center shadow-sm">
            <Card.Body>
                <Card.Title className="mb-4" style={{fontSize: "1.5rem", fontWeight: "bold"}}>
                    Waiting for the server to start the session...
                </Card.Title>
                <Card.Img
                    variant="top"
                    src="/nyancat-rainbow-cat.gif"
                    alt="Waiting for host"
                    style={{width: "200px", margin: "0 auto"}}
                />
            </Card.Body>
        </Card>
    </Container>
);

export default WaitForHost;