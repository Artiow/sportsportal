import React from 'react';
import './StarRate.css'

/**
 * StarRate by font awesome carousel.
 * @author Artem Namednev <namednev.artem@gmail.com>
 * @param props {object} component props
 * @return rate component
 */
export default function StarRate(props) {
    let resultClass = 'StarRate';
    const {value, className, ...otherProps} = props;
    if (className) resultClass = resultClass + ` ${className}`;
    let stars = [];
    let i = 0;
    while (i <= (value - 2)) {
        stars.push(<i key={i} className="fa fa-star-rate fa-star"/>);
        i += 2;
    }
    if (i < value) {
        stars.push(<i key={i} className="fa fa-star-rate fa-star-half-o"/>);
        i += 2;
    }
    while (i < 10) {
        stars.push(<i key={i} className="fa fa-star-rate fa-star-o"/>);
        i += 2;
    }
    return (
        <span className={resultClass} {...otherProps}>{stars}</span>
    );
}