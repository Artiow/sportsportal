import React from "react";
import 'rc-slider/assets/index.css';
import './Playground.css';

function Playground(props) {
    return (
        <main className="Playground container">
            <h1 className="display-4">Playground {props.identifier}</h1>
        </main>
    );
}

export default Playground;