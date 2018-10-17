import React from 'react';
import './ContentRow.css'

export default function ContentRow(props) {
    const {className, ...otherProps} = props;
    let resultClass = 'ContentRow content-row row';
    if (className) resultClass = resultClass + ` ${className}`;
    return (
        <div className={resultClass}>
            {otherProps.children}
        </div>
    );
}