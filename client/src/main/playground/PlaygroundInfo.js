import React from 'react';
import PlaygroundLeaseCalendar from './PlaygroundLeaseCalendar';
import PhotoCarousel from '../../util/components/PhotoCarousel';
import noImage from '../../util/img/no-image-grey-mdh.jpg';
import StarRate from '../../util/components/StarRate';
import apiUrl from '../../boot/constants';
import axios from 'axios';
import './PlaygroundInfo.css';

export default class PlaygroundInfo extends React.Component {
    constructor(props) {
        super(props);
        this.id = props.identifier;
        this.state = {content: null};
    }

    componentDidMount() {
        this.query();
    }

    query() {
        const self = this;
        axios.get(
            apiUrl('/leaseapi/playground/' + this.id)
        ).then(function (response) {
            console.debug('PlaygroundInfo (query):', response);
            self.setState({content: response.data});
        }).catch(function (error) {
            console.error('PlaygroundInfo (query):', ((error.response != null) ? error.response : error));
        })
    }

    render() {
        const photoExtractor = photoItems => {
            const photos = [];
            if (photoItems != null) photoItems.forEach(function (item, i, arr) {
                photos.push(item.url + '?size=mdh');
            });
            return photos;
        };
        const featureBuilder = capabilityItems => {
            const featureLines = [];
            if ((capabilityItems != null) && (capabilityItems.length > 0)) {
                capabilityItems.forEach(function (item, i, arr) {
                    const name = item.name;
                    featureLines.push(
                        <li className="list-group-item" key={i}>{(name.charAt(0).toUpperCase() + name.slice(1))}</li>
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
            <div className="PlaygroundInfo row">
                <div className="col-10 offset-1">
                    <div className="container content-container">
                        <div className="row header-row">
                            <div className="col-12">
                                <h1 className="row-h header-h header-name">
                                    {playground.name}
                                </h1>
                                <h4 className="row-h header-h header-address">
                                    {playground.address}
                                </h4>
                                <h6 className="row-h header-h header-rate">
                                    <StarRate value={playground.rate}/>
                                </h6>
                            </div>
                        </div>
                        <div className="row feature-row">
                            <div className="col-4">
                                <h4 className="row-h info-h info-price">
                                    <span className="mr-md-2">Стоимость:</span>
                                    <span className="badge badge-secondary">
                                                <span>{Math.floor(playground.price)}</span>
                                                <i className="fa fa-rub ml-1"/>/час
                                            </span>
                                </h4>
                                {(features != null) ? (
                                    <h5 className="row-h info-h info-infra">Инфраструктура:</h5>
                                ) : (null)}
                                {features}
                            </div>
                            <div className="col-8">
                                <PhotoCarousel photos={photos} placeimg={noImage}
                                               identifier="pg_photo_carousel"/>
                            </div>
                        </div>
                        <div className="row calendar-row">
                            <div className="col-12">
                                <h4 className="row-h calendar-h calendar-header">Аренда:</h4>
                                <PlaygroundLeaseCalendar identifier={this.id} version={playground.version}/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        ) : (null);
    }
}