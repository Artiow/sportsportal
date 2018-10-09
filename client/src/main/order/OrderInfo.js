import React from 'react';
import './OrderInfo.css';

export default function Order(props) {
    return (
        <div className="Order row">
            {'order id: ' + props.identifier}
        </div>
    );
}