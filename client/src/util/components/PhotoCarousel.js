import React from 'react';

/**
 * PhotoCarousel by bootstrap carousel.
 * @author Artem Namednev <namednev.artem@gmail.com>
 * @param props {object} component props
 * @return carousel component
 */
export default function PhotoCarousel(props) {
    let photos = props.photos;
    if ((photos == null) || (photos.length === 0)) {
        const placeimg = props.placeimg;
        photos = ((placeimg != null) ? [placeimg] : null);
    }

    if (photos != null) {
        const photosCount = photos.length;

        let carouselIndicator = null;
        if (photosCount > 1) {
            const carouselIndicatorItems = [(
                <li data-target="#carousel" data-slide-to="0" key={0} className="active"/>
            )];
            for (let i = 1; i < photosCount; i++) {
                carouselIndicatorItems.push(
                    <li data-target="#carousel" data-slide-to={i} key={i}/>
                )
            }
            carouselIndicator = (<ol className="carousel-indicators">{carouselIndicatorItems}</ol>);
        }

        const carouselInnerItems = [(
            <div className="carousel-item active" key={0}>
                <img className="d-block w-100" src={photos[0]} alt=""/>
            </div>
        )];
        for (let i = 1; i < photosCount; i++) {
            carouselInnerItems.push(
                <div className="carousel-item" key={i}>
                    <img className="d-block w-100" src={photos[i]} alt=""/>
                </div>
            );
        }
        const carouselInner = (<div className="carousel-inner">{carouselInnerItems}</div>);

        return (
            <div id="carousel" className="PhotoCarousel carousel slide" data-ride="carousel">
                {carouselIndicator}
                {carouselInner}
                {(photosCount > 1) ? (
                    <a className="carousel-control-prev" href="#carousel" role="button" data-slide="prev">
                        <span className="carousel-control-prev-icon" aria-hidden="true"/>
                        <span className="sr-only">Предыдущая</span>
                    </a>
                ) : (null)}
                {(photosCount > 1) ? (
                    <a className="carousel-control-next" href="#carousel" role="button" data-slide="next">
                        <span className="carousel-control-next-icon" aria-hidden="true"/>
                        <span className="sr-only">Следующая</span>
                    </a>
                ) : (null)}
            </div>
        )
    } else return (
        <div id="carousel" className="PhotoCarousel carousel slide" data-ride="carousel"/>
    );
}