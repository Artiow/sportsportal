import React from 'react';
import './PlacedImg.css';

/**
 * PlacedImg
 * @author Artem Namednev <namednev.artem@gmail.com>
 * @param props {object} component props
 * @return img component
 */
export default function PlacedImg(props) {
    const {alt, disabled, onError, placeImg, ...otherProps} = props;
    const img = (
        <img {...otherProps} alt={alt} onError={(event) => {
            event.target.onError = null;
            event.target.src = placeImg;
            if (typeof onError === 'function')
                onError(event);
        }}/>
    );
    return (!disabled) ? (img) : (<div className="placed-img-wrapper">{img}</div>);
}