import React from 'react';
import {Link} from "react-router-dom";
import CheckButton from '../../util/components/CheckButton';
import PlaygroundSubmitOrderModal from './PlaygroundSubmitOrderModal';
import {restoreReservation, saveReservation} from '../../util/reservationSaver';
import verify, {getToken} from '../../util/verification';
import apiUrl, {env} from '../../boot/constants';
import axios from 'axios';
import './PlaygroundLeaseCalendar.css';

export default class PlaygroundLeaseCalendar extends React.Component {

    static CANCEL_TITLE = 'Сбросить выбор';
    static SUBMIT_TITLE = 'Забронировать';

    static START_DATE_OFFSET = 0;
    static END_DATE_OFFSET = 6;

    constructor(props) {
        super(props);
        this.playgroundId = props.identifier;
        this.playgroundVersion = props.version;
        this.restoredReservation = restoreReservation(
            this.playgroundId,
            this.playgroundVersion
        );
        const start = PlaygroundLeaseCalendar.START_DATE_OFFSET;
        const end = PlaygroundLeaseCalendar.END_DATE_OFFSET;
        this.timeFrame = {
            offset: {
                start: start,
                end: end
            },
            date: {
                start: PlaygroundLeaseCalendar.normalNow(start),
                end: PlaygroundLeaseCalendar.normalNow(end)
            }
        };
        this.state = {
            price: null,
            schedule: null,
            dateList: null,
            timeList: null,
            reservation: null,
            halfHourAvailable: null,
            fullHourRequired: null,
            access: false
        };
        verify((data) => this.setState({
            access: !(data.login.roles.indexOf(env.ROLE.USER) < 0)
        }));
        this.query(
            this.playgroundId,
            this.playgroundVersion,
            this.timeFrame.date.start,
            this.timeFrame.date.end
        );
    }

    static normalNow(days) {
        return this.normalizeDate(this.now(days));
    }

    static normalizeDate(datetime) {
        let day = datetime.getDate();
        let month = datetime.getMonth();
        return datetime.getFullYear() + '-' + ((++month < 10) ? ('0' + month) : (month)) + '-' + ((day < 10) ? ('0' + day) : (day));
    }

    static normalizeTime(datetime) {
        let hours = datetime.getHours();
        let minutes = datetime.getMinutes();
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
        } else if ((btn === 'btn-prev') && (this.timeFrame.offset.start > 0)) {
            this.updateGrid(-1);
        }
    }

    updateGrid(offset) {
        if (offset !== 0) {
            const start = this.timeFrame.offset.start + offset;
            const end = this.timeFrame.offset.end + offset;
            this.timeFrame.offset.start = start;
            this.timeFrame.offset.end = end;
            this.timeFrame.date.start = PlaygroundLeaseCalendar.normalNow(start);
            this.timeFrame.date.end = PlaygroundLeaseCalendar.normalNow(end);
            this.query(
                this.playgroundId,
                this.playgroundVersion,
                this.timeFrame.date.start,
                this.timeFrame.date.end
            );
        }
    }

    query(id, version, from, to) {
        const self = this;
        axios.get(apiUrl('/leaseapi/playground/' + id + '/grid'), {params: {from: from, to: to}}
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
                const dateClass = new Date(date);
                dateList.push({
                    month: env.MONTH_NAMES[dateClass.getMonth()],
                    dayOfWeek: env.DAYS_OF_WEEK_NAMES[dateClass.getDay()],
                    date: date
                });
            });
            const schedule = new Map(array);
            const restoredReservation = self.restoredReservation;
            if (restoredReservation != null) {
                const reservation = [];
                restoredReservation.forEach(function (i, item, array) {
                    const datetime = i.split('T');
                    if ((timeList.indexOf(datetime[1]) >= 0) && schedule.get(datetime[0]) && (schedule.get(datetime[0])).get(datetime[1])) {
                        reservation.push(i)
                    }
                });
                self.restoredReservation = null;
                self.setState({reservation: reservation});
            }
            self.setState({
                price: price,
                dateList: dateList,
                timeList: timeList,
                schedule: schedule,
                halfHourAvailable: data.halfHourAvailable,
                fullHourRequired: data.fullHourRequired
            });
        }).catch(function (error) {
            console.log('Query Error:', ((error.response != null) ? error.response : error));
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
            const reservation = prevState.reservation;
            const schedule = prevState.schedule;
            const timeList = prevState.timeList;
            const checkActive = (prevState.halfHourAvailable && prevState.fullHourRequired);
            if (checkActive) {
                const updateByData = (element) => {
                    element.selected = (reservation.indexOf(element.value) >= 0);
                    element.available = ((timeList.indexOf(element.time) >= 0) && schedule.get(element.date) && (schedule.get(element.date)).get(element.time));
                };
                updateByData(farPrev);
                updateByData(prev);
                updateByData(next);
                updateByData(farNext);
            }
            const idx = reservation.indexOf(value);
            if ((checked) && (idx < 0)) {
                if (checkActive && !next.selected && !prev.selected) {
                    if (next.available && !next.selected) {
                        reservation.push(value);
                        reservation.push(next.value);
                    } else if (prev.available && !prev.selected) {
                        reservation.push(value);
                        reservation.push(prev.value);
                    } else {
                        console.log('WARNING: The selected time cell is incorrect!');
                    }
                } else reservation.push(value);
            } else {
                reservation.splice(idx, 1);
                const needNextRemoving = (next.selected && !farNext.selected);
                const needPrevRemoving = (prev.selected && !farPrev.selected);
                if (checkActive && (needNextRemoving || needPrevRemoving)) {
                    if (needNextRemoving) reservation.splice(reservation.indexOf(next.value), 1);
                    if (needPrevRemoving) reservation.splice(reservation.indexOf(prev.value), 1);
                }
            }
            saveReservation(this.playgroundId, this.playgroundVersion, reservation);
            return {reservation: reservation};
        });
    }

    submit(event) {
        event.preventDefault();
        axios.post(apiUrl('/leaseapi/playground/' + this.playgroundId + '/reserve'), {
            reservations: this.state.reservation
        }, {
            headers: {Authorization: getToken(),}
        }).then(function (response) {
            console.log('Query Response:', response);
            const locationArray = response.headers.location.split('/');
            // todo: soft redirect!
            window.location.replace('/order/id' + locationArray[locationArray.length - 1]);
        }).catch(function (error) {
            console.log('Query Error:', ((error.response != null) ? error.response : error));
            // todo: show error!
        });
    }

    render() {
        const cancel = event => {
            this.setState({reservation: []});
            saveReservation(this.playgroundId, this.playgroundVersion, []);
        };
        const headerLineBuilder = dateList => {
            const headerLine = [];
            if ((dateList != null) && (dateList.length > 0)) {
                dateList.forEach(function (item, i, arr) {
                    headerLine.push(
                        <th key={i} className="th-calendar">
                            <div className="small">{item.month}</div>
                            <div>
                                <span className="mr-1">{item.date.split('-')[2]}</span>
                                <span className="ml-1">{item.dayOfWeek.short.toUpperCase()}</span>
                            </div>
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
                            {value.get(item) ? (
                                <CheckButton id={datetime} value={datetime} content={content}
                                             checked={!(reservation.indexOf(datetime) < 0)}
                                             onChange={updateReservation}/>
                            ) : (
                                <button className="btn btn-sm btn-light disabled"
                                        disabled="disabled">
                                    {content}
                                </button>
                            )}
                        </td>
                    )
                });
                table.push(<tr key={i}>{rows}</tr>)
            });
            return (table);
        };
        const access = this.state.access;
        const schedule = this.state.schedule;
        const reservation = this.state.reservation;
        const price = this.state.price;
        const totalPrice = ((reservation != null) && (price != null)) ? reservation.length * price : 0;
        if (schedule != null) {
            return (
                <form className="PlaygroundLeaseCalendar" onSubmit={this.submit.bind(this)}>
                    <table className="table table-hover">
                        <thead className="thead-dark">
                        <tr>
                            <th className="th-control">
                                {(this.timeFrame.offset.start > 0) ? (
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
                        {tableBuilder(this.state.timeList, price, schedule)}
                        </tbody>
                    </table>
                    <div className="order-group">
                        {(!access) ? (
                            <div className="alert alert-danger" role="alert">
                                Необходимо <Link to="/?login" className="alert-link">авторизироваться</Link> для того,
                                чтобы сделать заказ!
                            </div>
                        ) : (null)}
                        {(reservation.length > 0) ? (
                            <div className="btn-group">
                                <button type="button" className="btn btn-danger" onClick={cancel}>
                                    {PlaygroundLeaseCalendar.CANCEL_TITLE}
                                </button>
                                <button type="button" className="btn btn-success" data-toggle="modal"
                                        data-target="#submitOrder" disabled={!access}>
                                    {PlaygroundLeaseCalendar.SUBMIT_TITLE}
                                    <span className="badge badge-dark ml-1">
                                    {totalPrice}<i className="fa fa-rub ml-1"/>
                                </span>
                                </button>
                            </div>
                        ) : (
                            <div className="btn-group">
                                <button className="btn btn-danger disabled" disabled="disabled">
                                    {PlaygroundLeaseCalendar.CANCEL_TITLE}
                                </button>
                                <button className="btn btn-success disabled" disabled="disabled">
                                    {PlaygroundLeaseCalendar.SUBMIT_TITLE}
                                    <span className="badge badge-dark ml-1">
                                    {totalPrice}<i className="fa fa-rub ml-1"/>
                                </span>
                                </button>
                            </div>
                        )}
                    </div>
                    <PlaygroundSubmitOrderModal identifier="submitOrder"/>
                </form>
            )
        } else {
            return null;
        }
    }
}