import React from 'react';
import './Order.css';

export default function Order(props) {
    return (
        <main className="Order container">
            {'order id: ' + props.identifier}
        </main>
    );
}