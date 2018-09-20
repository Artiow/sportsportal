import React, {Component} from 'react';
import PhotoCarousel from '../util/components/PhotoCarousel';
import StarRate from '../util/components/StarRate';
import {getApiUrl} from '../boot/constants';
import axios from 'axios';
import './Playground.css';
import noImage from '../util/img/no-image-grey-mdh.jpg';

class Playground extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.identifier,
            content: null
        };
        this.queryOnLoad();
    }

    queryOnLoad() {
        const self = this;
        axios.get(
            getApiUrl('/leaseapi/playground/' + this.state.id)
        ).then(function (response) {
            console.log('Query Response:', response);
            self.setState({content: response.data});
        }).catch(function (error) {
            console.log('Query Error:', error);
        })
    }

    render() {
        const playground = this.state.content;
        const isLoad = (playground != null);
        let photos = [];
        let features = null;
        if (isLoad) {
            const photoItems = playground.photos;
            if (photoItems != null) photoItems.forEach(function (item, i, arr) {
                photos.push(item.url + '?size=mdh');
            });

            const featureLines = [];
            const capabilities = playground.capabilities;
            if ((capabilities != null) && (capabilities.length > 0)) {
                capabilities.forEach(function (item, i, arr) {
                    const name = item.name;
                    featureLines.push(<li className="list-group-item" key={i}>{(name.charAt(0).toUpperCase() +
                        name.slice(1))}</li>);
                });
                features = (<ul className="list-group list-group-flush">{featureLines}</ul>)
            }
        }
        return (
            <main className="Playground container">
                {isLoad ? (
                    <div className="row">
                        <div className="col-10 offset-1">
                            <div className="container content-container">
                                <div className="row header-row">
                                    <div className="col-12">
                                        <h1>{playground.name}</h1>
                                        <h4>{playground.address}</h4>
                                        <h6><StarRate value={playground.rate}/></h6>
                                    </div>
                                </div>
                                <div className="row info-row">
                                    <div className="col-4">
                                        <h4>
                                            <span className="mr-md-2">Стоимость:</span>
                                            <span className="badge badge-secondary">
                                                <span>{((playground.cost / 100).toFixed())}</span>
                                                <i className="fa fa-rub"/>/час
                                            </span>
                                        </h4>
                                        {(features != null) ? (<h5>Инфраструктура:</h5>) : (null)}
                                        {features}
                                    </div>
                                    <div className="col-8">
                                        <PhotoCarousel photos={photos} placeimg={noImage}/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                ) : (null)}
            </main>
        );
    }
}

export default Playground;