import React from 'react';
import Playground from '../../connector/Playground';
import PlaygroundBookingCalendar from './components/PlaygroundBookingCalendar';
import ContentContainer from '../../util/components/special/ContentContainer';
import ContentRow from '../../util/components/special/ContentRow';
import PhotoCarousel from '../../util/components/PhotoCarousel';
import noImage from '../../util/img/no-image-grey-mdh.jpg';
import StarRate from '../../util/components/StarRate';
import './PlaygroundPage.css';

export default class PlaygroundPage extends React.Component {
    constructor(props) {
        super(props);
        this.id = props.identifier;
        this.state = {content: null};
    }

    componentDidMount() {
        Playground.get(this.id)
            .then(data => {
                console.debug('PlaygroundPage', 'query', 'success');
                this.setState({content: data});
            })
            .catch(error => {
                console.error('PlaygroundPage', 'query', 'failed');
            });
    }

    render() {
        const photoExtractor = photoItems => {
            const photos = [];
            if (photoItems != null) {
                photoItems.forEach(value => photos.push(value.pictureURL + '?size=mdh'));
            }
            return photos;
        };
        const featureBuilder = capabilityItems => {
            const featureLines = [];
            if ((capabilityItems != null) && (capabilityItems.length > 0)) {
                capabilityItems.forEach((value, index) => {
                    const name = value.name;
                    featureLines.push(
                        <li className="list-group-item" key={index}>
                            {(name.charAt(0).toUpperCase() + name.slice(1))}
                        </li>
                    );
                });
                return (<ul className="list-group list-group-flush">{featureLines}</ul>);
            } else {
                return null;
            }
        };
        const playground = this.state.content;
        const didLoad = (playground != null);
        const photos = didLoad ? photoExtractor(playground.photos) : null;
        const features = didLoad ? featureBuilder(playground.capabilities) : null;
        return didLoad ? (
            <ContentContainer className="PlaygroundPage">
                <ContentRow className="header">
                    <div className="col-12">
                        <h1 className="header">{playground.name}</h1>
                        <h4>{playground.address}</h4>
                        <h6><StarRate value={playground.rate}/></h6>
                    </div>
                </ContentRow>
                <ContentRow className="feature">
                    <div className="col-4">
                        <h4 className="row-h info-h info-price">
                            <span className="mr-md-2">Стоимость:</span>
                            {(playground.isFreed) ? (
                                <span className="badge badge-secondary">
                                    отсутствует
                                </span>
                            ) : (
                                <span className="badge badge-secondary">
                                    <span>{Math.floor(playground.price)}</span><i className="fa fa-rub ml-1"/>/час
                                </span>
                            )}
                        </h4>
                        {(features != null) ? (
                            <h5 className="feature">Инфраструктура:</h5>
                        ) : (null)}
                        {features}
                    </div>
                    <div className="col-8">
                        <PhotoCarousel identifier="pg_photo_carousel" photos={photos} placeimg={noImage}/>
                    </div>
                </ContentRow>
                <ContentRow className="calendar">
                    <div className="col-12">
                        {(playground.isFreed) ? (
                            <h4 className="row-h calendar-h calendar-header">Бронирование:</h4>
                        ) : (
                            <h4 className="row-h calendar-h calendar-header">Аренда:</h4>
                        )}
                        <PlaygroundBookingCalendar identifier={this.id} version={playground.version}/>
                    </div>
                </ContentRow>
            </ContentContainer>
        ) : (null);
    }
}