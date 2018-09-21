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
                    featureLines.push(
                        <li className="list-group-item" key={i}>
                            {(name.charAt(0).toUpperCase() + name.slice(1))}
                        </li>
                    );
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
                                <div className="row info-row header-info-row">
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
                                <div className="row info-row feature-info-row">
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
                                        <PhotoCarousel photos={photos} placeimg={noImage}/>
                                    </div>
                                </div>
                                <div className="row info-row lease-info-row">
                                    <div className="col-12">
                                        <h4 className="row-h info-h info-price">Аренда:</h4>
                                        <PlaygroundLeaseCalendar/>
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

class PlaygroundLeaseCalendar extends Component {

    static START_DATE_OFFSET = 0;
    static END_DATE_OFFSET = 6;
    static DAYS_OF_WEEK_NAMES = ['воскресенье', 'понедельник', 'вторник', 'среда', 'четверг', 'пятница', 'суббота'];

    constructor(props) {
        super(props);
        const sOffset = PlaygroundLeaseCalendar.START_DATE_OFFSET;
        const eOffset = PlaygroundLeaseCalendar.END_DATE_OFFSET;
        this.state = {
            startDateOffset: sOffset,
            endDateOffset: eOffset,
            daysOfWeek: PlaygroundLeaseCalendar.daysOfWeek(sOffset, eOffset),
            startDate: PlaygroundLeaseCalendar.normalNow(sOffset),
            endDate: PlaygroundLeaseCalendar.normalNow(eOffset),
        };
    }

    static daysOfWeek(from, to) {
        const daysOfWeek = [];
        for (let i = from; i <= to; i++) {
            let now = this.now(i);
            daysOfWeek.push({
                date: this.normalize(now),
                dayOfWeek: this.DAYS_OF_WEEK_NAMES[now.getDay()]
            })
        }
        return daysOfWeek;
    }

    static normalNow(days) {
        return this.normalize(this.now(days));
    }

    static normalize(date) {
        let day = date.getDate();
        let month = date.getMonth();
        return date.getFullYear() + '-' + ((++month < 10) ? ('0' + month) : (month)) + '-' + ((day < 10) ? ('0' + day) : (day));
    }

    static now(days) {
        const today = new Date();
        return ((days != null) && (days > 0))
            ? new Date(today.getFullYear(), today.getMonth(), (today.getDate() + days))
            : today;
    }

    updateGridInfo(newStartDateOffset, newEndDateOffset) {
        this.setState({
            startDateOffset: newStartDateOffset,
            endDateOffset: newEndDateOffset,
            daysOfWeek: PlaygroundLeaseCalendar.daysOfWeek(newStartDateOffset, newEndDateOffset),
            startDate: PlaygroundLeaseCalendar.normalNow(newStartDateOffset),
            endDate: PlaygroundLeaseCalendar.normalNow(newEndDateOffset),
        })
    }

    render() {
        const tmp = [];
        this.state.daysOfWeek.forEach(function (item, i, arr) {
            tmp.push(<span key={i}>{item.date + ' (' + item.dayOfWeek + ')'}</span>);
            tmp.push(<br/>);
        });
        return (
            <div className="PlaygroundLeaseCalendar">
                <span>{this.state.startDate}</span>
                <br/>
                <span>{this.state.endDate}</span>
                <br/>
                <br/>
                <span>{tmp}</span>
            </div>
        )
    }
}

export default Playground;