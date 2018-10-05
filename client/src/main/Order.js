import React from 'react';
import './Order.css';

export default function Order(props) {
    return (
        <div className="row">
            {'order id: ' + props.identifier}
        </div>
    );
}