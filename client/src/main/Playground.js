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
        this.id = props.identifier;
        this.state = {content: null};
        this.queryOnLoad();
    }

    queryOnLoad() {
        const self = this;
        axios.get(
            getApiUrl('/leaseapi/playground/' + this.id)
        ).then(function (response) {
            console.log('Query Response:', response);
            self.setState({content: response.data});
        }).catch(function (error) {
            console.log('Query Error:', error.response);
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
        const isLoad = (playground != null);
        const photos = (isLoad) ? photoExtractor(playground.photos) : null;
        const features = (isLoad) ? featureBuilder(playground.capabilities) : null;
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
                                        <PlaygroundLeaseCalendar identifier={this.id}/>
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
    static DAYS_OF_WEEK_NAMES = [
        {
            full: 'воскресенье',
            short: 'вс'
        },
        {
            full: 'понедельник',
            short: 'пн'
        },
        {
            full: 'вторник',
            short: 'вт'
        },
        {
            full: 'среда',
            short: 'ср'
        },
        {
            full: 'четверг',
            short: 'чт'
        },
        {
            full: 'пятница',
            short: 'пт'
        },
        {
            full: 'суббота',
            short: 'сб'
        }
    ];

    constructor(props) {
        super(props);
        this.playgroundId = props.identifier;
        const sOffset = PlaygroundLeaseCalendar.START_DATE_OFFSET;
        const eOffset = PlaygroundLeaseCalendar.END_DATE_OFFSET;
        this.offset = {
            startDateOffset: sOffset,
            endDateOffset: eOffset,
            startDate: PlaygroundLeaseCalendar.normalNow(sOffset),
            endDate: PlaygroundLeaseCalendar.normalNow(eOffset)
        };
        this.state = {
            schedule: null,
            daysOfWeek: PlaygroundLeaseCalendar.daysOfWeek(
                this.offset.startDateOffset,
                this.offset.endDateOffset
            )
        };
        this.query(
            this.playgroundId,
            this.offset.startDate,
            this.offset.endDate
        );
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

    handleOffset(event) {
        let btn = event.target.id;
        if ((btn == null) || (btn === '')) btn = event.target.parentNode.id;
        if (btn === 'btn-next') {
            this.updateGrid(1);
        } else if ((btn === 'btn-prev') && (this.offset.startDateOffset > 0)) {
            this.updateGrid(-1);
        }
    }

    updateGrid(offset) {
        if (offset !== 0) {
            const sOffset = this.offset.startDateOffset + offset;
            const eOffset = this.offset.endDateOffset + offset;
            this.offset.startDateOffset = sOffset;
            this.offset.endDateOffset = eOffset;
            this.offset.startDate = PlaygroundLeaseCalendar.normalNow(sOffset);
            this.offset.endDate = PlaygroundLeaseCalendar.normalNow(eOffset);
            this.setState({
                daysOfWeek: PlaygroundLeaseCalendar.daysOfWeek(
                    this.offset.startDateOffset,
                    this.offset.endDateOffset
                )
            });
            this.query(
                this.playgroundId,
                this.offset.startDate,
                this.offset.endDate
            );
        }
    }

    query(id, from, to) {
        const self = this;
        const url = getApiUrl('/leaseapi/playground/' + id + '/grid');
        axios.get(url, {params: {from: from, to: to}}
        ).then(function (response) {
            console.log('Query Response:', response);
            self.setState({schedule: response.data.grid.schedule});
        }).catch(function (error) {
            console.log('Query Error:', error);
            console.log('Query Error:', error.response);
        });
    }

    render() {
        const headerLine = [];
        const daysOfWeek = this.state.daysOfWeek;
        daysOfWeek.forEach(function (item, i, arr) {
            headerLine.push(
                <th key={i} className="th-calendar">
                    <span className="mr-1">{item.date.split('-')[2]}</span>
                    <span className="ml-1">{item.dayOfWeek.short.toUpperCase()}</span>
                </th>
            );
        });

        return (
            <div className="PlaygroundLeaseCalendar">
                <table className="table table-hover mt-3">
                    <thead className="thead-dark">
                    <tr>
                        <th className="th-control">
                            <button type="button" id="btn-prev" className="btn btn-sm btn-outline-info"
                                    title="Назад" onClick={this.handleOffset.bind(this)}>
                                <i className="fa fa-angle-left"/>
                            </button>
                            <button type="button" id="btn-next" className="btn btn-sm btn-outline-info"
                                    title="Вперед" onClick={this.handleOffset.bind(this)}>
                                <i className="fa fa-angle-right"/>
                            </button>
                        </th>
                        {headerLine}
                    </tr>
                    </thead>
                    <tbody/>
                </table>
            </div>
        )
    }
}

export default Playground;