import React from 'react';

/**
 * PlacedImg
 * @author Artem Namednev <namednev.artem@gmail.com>
 * @param props {object} component props
 * @return img component
 */
export default function PlacedImg(props) {
    const {alt, onError, placeImg, ...otherProps} = props;
    return (
        <img {...otherProps} alt={alt} onError={(event) => {
            event.target.onError = null;
            event.target.src = placeImg;
            if (typeof onError === 'function')
                onError(event);
        }}/>
    )
}