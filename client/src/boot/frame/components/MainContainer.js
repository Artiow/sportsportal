import React from "react";
import './MainContainer.css';

export default function MainContainer(props) {
    return (
        <main className="MainContainer container">
            {props.children}
        </main>
    );
}