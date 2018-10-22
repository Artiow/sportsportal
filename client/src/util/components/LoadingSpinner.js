import React from 'react';
import './LoadingSpinner.css'

export default function LoadingSpinner(props) {
    const {className, ...otherProps} = props;
    let resultClass = 'LoadingSpinner';
    if (className) resultClass = resultClass + ` ${className}`;
    return (
        <div className={resultClass} {...otherProps}>
            <i className="fa fa-refresh fa-spin fa-5x fa-fw"/>
        </div>
    );
}