import React from 'react';
import './ContentContainer.css'

export default function ContentContainer(props) {
    const {className, ...otherProps} = props;
    let resultClass = 'ContentContainer row';
    if (className) resultClass = resultClass + ` ${className}`;
    return (
        <div className={resultClass} {...otherProps}>
            <div className="col-10 offset-1">
                <div className="content-container container">
                    {otherProps.children}
                </div>
            </div>
        </div>
    );
}