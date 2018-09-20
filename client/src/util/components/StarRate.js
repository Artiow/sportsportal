import React from "react";

/**
 * StarRate by font awesome carousel.
 * @author Artem Namednev <namednev.artem@gmail.com>
 * @param props {object} component props
 * @return rate component
 */
function StarRate(props) {
    const value = props.value;
    let stars = [];
    let i = 0;
    while (i <= (value - 2)) {
        stars.push(<i key={i} className="fa fa-sr fa-star"/>);
        i += 2;
    }
    if (i < value) {
        stars.push(<i key={i} className="fa fa-sr fa-star-half-o"/>);
        i += 2;
    }
    while (i < 10) {
        stars.push(<i key={i} className="fa fa-sr fa-star-o"/>);
        i += 2;
    }
    return (
        <span className="StarRate span-sr">{stars}</span>
    );
}

export default StarRate;