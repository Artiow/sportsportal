import React, {Component} from 'react';
import PhotoCarousel from '../util/components/PhotoCarousel';
import CheckButton from '../util/components/CheckButton';
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
        const didLoad = (playground != null);
        const photos = didLoad ? photoExtractor(playground.photos) : null;
        const features = didLoad ? featureBuilder(playground.capabilities) : null;
        return (
            <main className="Playground container">
                {didLoad ? (
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
                                <div className="row info-row calendar-info-row">
                                    <div className="col-12">
                                        <h4 className="row-h calendar-h calendar-header">Аренда:</h4>
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
    static DAYS_OF_WEEK_NAMES = [{
        full: 'воскресенье',
        short: 'вс'
    }, {
        full: 'понедельник',
        short: 'пн'
    }, {
        full: 'вторник',
        short: 'вт'
    }, {
        full: 'среда',
        short: 'ср'
    }, {
        full: 'четверг',
        short: 'чт'
    }, {
        full: 'пятница',
        short: 'пт'
    }, {
        full: 'суббота',
        short: 'сб'
    }];

    constructor(props) {
        super(props);
        this.playgroundId = props.identifier;
        const start = PlaygroundLeaseCalendar.START_DATE_OFFSET;
        const end = PlaygroundLeaseCalendar.END_DATE_OFFSET;
        this.frame = {
            offset: {
                start: start,
                end: end
            }, date: {
                start: PlaygroundLeaseCalendar.normalNowDate(start),
                end: PlaygroundLeaseCalendar.normalNowDate(end)
            }
        };
        this.state = {
            price: null,
            schedule: null,
            dateList: null,
            timeList: null,
            reservation: [],
            halfHourAvailable: false,
            fullHourRequired: false
        };
        this.query(
            this.playgroundId,
            this.frame.date.start,
            this.frame.date.end
        );
    }

    static normalNowDate(days) {
        return this.normalizeDate(this.now(days));
    }

    static normalize(date) {
        return this.normalizeDate(date) + 'T' + this.normalizeTime(date);
    }

    static normalizeDate(date) {
        let day = date.getDate();
        let month = date.getMonth();
        return date.getFullYear() + '-' + ((++month < 10) ? ('0' + month) : (month)) + '-' + ((day < 10) ? ('0' + day) : (day));
    }

    static normalizeTime(date) {
        let hours = date.getHours();
        let minutes = date.getMinutes();
        return ((hours < 10) ? ('0' + hours) : (hours)) + ':' + ((minutes < 10) ? ('0' + minutes) : (minutes));
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
        } else if ((btn === 'btn-prev') && (this.frame.offset.start > 0)) {
            this.updateGrid(-1);
        }
    }

    updateGrid(offset) {
        if (offset !== 0) {
            const start = this.frame.offset.start + offset;
            const end = this.frame.offset.end + offset;
            this.frame.offset.start = start;
            this.frame.offset.end = end;
            this.frame.date.start = PlaygroundLeaseCalendar.normalNowDate(start);
            this.frame.date.end = PlaygroundLeaseCalendar.normalNowDate(end);
            this.query(
                this.playgroundId,
                this.frame.date.start,
                this.frame.date.end
            );
        }
    }

    query(id, from, to) {
        const self = this;
        const url = getApiUrl('/leaseapi/playground/' + id + '/grid');
        axios.get(url, {params: {from: from, to: to}}
        ).then(function (response) {
            console.log('Query Response:', response);
            const dateList = [];
            const timeList = [];
            const data = response.data;
            const price = data.halfHourAvailable ? Math.floor(data.price / 2) : data.price;
            const array = Object.entries(data.grid.schedule);
            Object.entries(array[0][1]).forEach(item => {
                timeList.push(item[0])
            });
            array.forEach(function (item, i, arr) {
                arr[i][1] = new Map(Object.entries(item[1]));
                const date = item[0];
                dateList.push({
                    dayOfWeek: PlaygroundLeaseCalendar.DAYS_OF_WEEK_NAMES[(new Date(date)).getDay()],
                    date: date
                });
            });
            self.setState({
                price: price,
                dateList: dateList,
                timeList: timeList,
                schedule: new Map(array),
                halfHourAvailable: data.halfHourAvailable,
                fullHourRequired: data.fullHourRequired
            });
        }).catch(function (error) {
            console.log('Query Error:', error);
            console.log('Query Error:', error.response);
        });
    }

    updateReservation(event) {
        const value = event.target.value;
        const checked = event.target.checked;
        const buildByOffset = minuteDiff => {
            const valueDatetime = new Date(value);
            const datetime = new Date(new Date(value).setMinutes(valueDatetime.getMinutes() + minuteDiff));
            const date = PlaygroundLeaseCalendar.normalizeDate(datetime);
            const time = PlaygroundLeaseCalendar.normalizeTime(datetime);
            return {
                date: date,
                time: time,
                value: (date + 'T' + time),
                notSelected: undefined,
                available: undefined
            }
        };
        const farPrev = buildByOffset(-60);
        const prev = buildByOffset(-30);
        const next = buildByOffset(+30);
        const farNext = buildByOffset(+60);
        this.setState(prevState => {
            const arr = prevState.reservation;
            const schedule = prevState.schedule;
            const timeList = prevState.timeList;
            const checkActive = (prevState.halfHourAvailable && prevState.fullHourRequired);
            if (checkActive) {
                const updateByData = (element) => {
                    element.selected = (arr.indexOf(element.value) >= 0);
                    element.available = ((timeList.indexOf(element.time) >= 0) && (schedule.get(element.date)).get(element.time));
                };
                updateByData(farPrev);
                updateByData(prev);
                updateByData(next);
                updateByData(farNext);
            }
            const idx = arr.indexOf(value);
            if ((checked) && (idx < 0)) {
                if (checkActive && !next.selected && !prev.selected) {
                    if (next.available && !next.selected) {
                        arr.push(value);
                        arr.push(next.value);
                    } else if (prev.available && !prev.selected) {
                        arr.push(value);
                        arr.push(prev.value);
                    } else {
                        console.log('WARNING: The selected time cell is incorrect!');
                    }
                } else arr.push(value);
            } else {
                arr.splice(idx, 1);
                const needNextRemoving = (next.selected && !farNext.selected);
                const needPrevRemoving = (prev.selected && !farPrev.selected);
                if (checkActive && (needNextRemoving || needPrevRemoving)) {
                    if (needNextRemoving) arr.splice(arr.indexOf(next.value), 1);
                    if (needPrevRemoving) arr.splice(arr.indexOf(prev.value), 1);
                }
            }
            return {reservation: arr};
        });
    }

    render() {
        const headerLineBuilder = dateList => {
            const headerLine = [];
            if ((dateList != null) && (dateList.length > 0)) {
                dateList.forEach(function (item, i, arr) {
                    headerLine.push(
                        <th key={i} className="th-calendar">
                            <span className="mr-1">{item.date.split('-')[2]}</span>
                            <span className="ml-1">{item.dayOfWeek.short.toUpperCase()}</span>
                        </th>
                    );
                });
            }
            return headerLine;
        };
        const tableBuilder = (timeList, price, schedule) => {
            const table = [];
            const reservation = this.state.reservation;
            const updateReservation = this.updateReservation.bind(this);
            timeList.forEach(function (item, i, arr) {
                const rows = [(<td key={0}><span className="badge badge-secondary">{item}</span></td>)];
                schedule.forEach(function (value, key, map) {
                    const content = (<span>{price}<i className="fa fa-rub ml-1"/></span>);
                    const datetime = key + 'T' + item;
                    rows.push(
                        <td key={rows.length}>
                            {(value.get(item)
                                    ? (<CheckButton id={datetime} value={datetime} content={content}
                                                    checked={!(reservation.indexOf(datetime) < 0)}
                                                    onChange={updateReservation}/>)
                                    : (<button className="btn btn-sm btn-light disabled">{content}</button>)
                            )}
                        </td>
                    )
                });
                table.push(<tr key={i}>{rows}</tr>)
            });
            return (table);
        };
        const schedule = this.state.schedule;
        if (schedule != null) {
            return (
                <table className="PlaygroundLeaseCalendar table table-hover">
                    <thead className="thead-dark">
                    <tr>
                        <th>
                            {this.frame.offset.start > 0 ? (
                                <button type="button" id="btn-prev" className="btn btn-sm btn-outline-light"
                                        title="Назад" onClick={this.handleOffset.bind(this)}>
                                    <i className="fa fa-angle-left"/>
                                </button>
                            ) : (
                                <button disabled="disabled" className="btn btn-sm btn-outline-secondary disabled"
                                        title="Назад">
                                    <i className="fa fa-angle-left"/>
                                </button>
                            )}
                            <button type="button" id="btn-next" className="btn btn-sm btn-outline-light"
                                    title="Вперед" onClick={this.handleOffset.bind(this)}>
                                <i className="fa fa-angle-right"/>
                            </button>
                        </th>
                        {headerLineBuilder(this.state.dateList)}
                    </tr>
                    </thead>
                    <tbody>
                    {tableBuilder(this.state.timeList, this.state.price, schedule)}
                    </tbody>
                </table>
            )
        } else {
            return null;
        }
    }
}

export default Playground;