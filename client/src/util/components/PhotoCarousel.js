import React from 'react';

/**
 * PhotoCarousel by bootstrap carousel.
 * @author Artem Namednev <namednev.artem@gmail.com>
 * @param props {object} component props
 * @return carousel component
 */
export default function PhotoCarousel(props) {
    const placeimg = props.placeimg;
    const identifier = props.identifier ? props.identifier : 'carousel';
    const photos = ((props.photos == null) || (props.photos.length === 0))
        ? (placeimg ? [props.placeimg] : null)
        : props.photos;

    if (photos != null) {
        const photosCount = photos.length;
        const carouselIndicatorItem = (key, active) => {
            return (
                <li data-target={identifier} data-slide-to={key} key={key} className={active ? 'active' : undefined}/>
            )
        };
        const carouselInnerItem = (key, active) => {
            return (
                <div className={active ? 'carousel-item active' : 'carousel-item'} key={key}>
                    <img className="d-block w-100" src={photos[key]} alt={`${key}`}
                         onError={(e) => {
                             e.target.onError = null;
                             e.target.src = placeimg
                         }}/>
                </div>
            );
        };

        let carouselIndicator = null;
        if (photosCount > 1) {
            const carouselIndicatorItems = [carouselIndicatorItem(0, true)];
            for (let i = 1; i < photosCount; i++) carouselIndicatorItems.push(carouselIndicatorItem(i, false))
            carouselIndicator = (<ol className="carousel-indicators">{carouselIndicatorItems}</ol>);
        }

        const carouselInnerItems = [carouselInnerItem(0, true)];
        for (let i = 1; i < photosCount; i++) carouselInnerItems.push(carouselInnerItem(i, false));
        const carouselInner = (<div className="carousel-inner">{carouselInnerItems}</div>);

        return (
            <div id={identifier} className="PhotoCarousel carousel slide" data-ride={identifier}>
                {carouselIndicator}
                {carouselInner}
                {(photosCount > 1) ? (
                    <a className="carousel-control-prev" href={`#${identifier}`} role="button" data-slide="prev">
                        <span className="carousel-control-prev-icon" aria-hidden="true"/>
                        <span className="sr-only">Предыдущая</span>
                    </a>
                ) : (null)}
                {(photosCount > 1) ? (
                    <a className="carousel-control-next" href={`#${identifier}`} role="button" data-slide="next">
                        <span className="carousel-control-next-icon" aria-hidden="true"/>
                        <span className="sr-only">Следующая</span>
                    </a>
                ) : (null)}
            </div>
        )
    } else return (
        <div id={identifier} className="PhotoCarousel carousel slide" data-ride={identifier}/>
    );
}