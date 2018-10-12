import React from 'react';
import {Link} from "react-router-dom";
import MainFrame from '../../boot/frame/MainFrame'
import CheckButton from '../../util/components/CheckButton';
import PlaygroundSubmitOrderModal from './PlaygroundSubmitOrderModal';
import {restoreReservation, saveReservation} from '../../util/reservationSaver';
import verify, {getToken} from '../../util/verification';
import apiUrl, {env} from '../../boot/constants';
import axios from 'axios';
import './PlaygroundLeaseCalendar.css';

export default class PlaygroundLeaseCalendar extends React.Component {

    static CLOSE_TITLE = 'Отмена';
    static CANCEL_TITLE = 'Сбросить выбор';
    static USER_SUBMIT_TITLE = 'Забронировать';
    static OWNER_SUBMIT_TITLE = 'Зарезервировать';

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
        this.ownersURLs = null;
        this.userURL = null;
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
            access: false,
            owner: false
        };
        verify((data) => {
            const login = data.login;
            this.setState({access: !(login.roles.indexOf(env.ROLE.USER) < 0)});
            this.userURL = login.userURL;
            this.setAuthority();
        });
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

    setAuthority() {
        const user = this.userURL;
        const owners = this.ownersURLs;
        this.setState({owner: ((user != null) && (owners != null)) ? !(owners.indexOf(user) < 0) : false})
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
            axios.get(data.playgroundURL + '/short')
                .then(response => {
                    self.ownersURLs = response.data.ownersURLs;
                    self.setAuthority();
                });
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
            const restoredReservation = self.restoredReservation; // todo: bag: restored only firs 7 days!
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
            const checkedStyle = this.state.owner ? 'btn-primary' : 'btn-success';
            const updateReservation = this.updateReservation.bind(this);
            timeList.forEach(function (item, i, arr) {
                const rows = [(<td key={0}><span className="badge badge-secondary">{item}</span></td>)];
                schedule.forEach(function (value, key, map) {
                    const content = (<span>{price}<i className="fa fa-rub ml-1"/></span>);
                    const datetime = key + 'T' + item;
                    rows.push(
                        <td key={rows.length}>
                            {value.get(item) ? (
                                <CheckButton value={datetime}
                                             sizeStyle="btn-sm"
                                             checkedStyle={checkedStyle}
                                             uncheckedStyle={'btn-outline-dark'}
                                             id={`pg_lease_calendar_button_${datetime}`}
                                             checked={!(reservation.indexOf(datetime) < 0)}
                                             onChange={updateReservation}>
                                    {content}
                                </CheckButton>
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
        const owner = this.state.owner;
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
                            <AuthAlert link="/login" onClick={MainFrame.reLogin}>авторизироваться</AuthAlert>
                        ) : (null)}
                        <div className="btn-group">
                            <CancelButton disabled={!(reservation.length > 0)} onClick={cancel}
                                          title={PlaygroundLeaseCalendar.CANCEL_TITLE}/>
                            <SubmitButton dataToggle="modal" dataTarget="#submitOrder"
                                          disabled={(!(reservation.length > 0) || !access)}
                                          userTitle={PlaygroundLeaseCalendar.USER_SUBMIT_TITLE}
                                          ownerTitle={PlaygroundLeaseCalendar.OWNER_SUBMIT_TITLE}
                                          owner={owner} price={totalPrice}/>
                        </div>
                    </div>
                    <PlaygroundSubmitOrderModal closeTitle={PlaygroundLeaseCalendar.CLOSE_TITLE}
                                                userTitle={PlaygroundLeaseCalendar.USER_SUBMIT_TITLE}
                                                ownerTitle={PlaygroundLeaseCalendar.OWNER_SUBMIT_TITLE}
                                                submitId="submitOrder" reservation={reservation}
                                                owner={owner} price={totalPrice}/>
                </form>
            )
        } else {
            return null;
        }
    }
}

function AuthAlert(props) {
    return (
        <div className="alert alert-danger">
            Необходимо <Link to={props.link} className="alert-link" onClick={props.onClick}>авторизироваться</Link> для
            того, чтобы сделать заказ!
        </div>
    )
}

function CancelButton(props) {
    return (
        <button type="button" disabled={props.disabled}
                className={props.disabled ? 'btn btn-danger disabled' : 'btn btn-danger'}
                onClick={props.disabled ? undefined : props.onClick}>
            {props.title}
        </button>
    );
}

function SubmitButton(props) {
    return (
        <button type="button" disabled={props.disabled}
                data-toggle={props.disabled ? undefined : props.dataToggle}
                data-target={props.disabled ? undefined : props.dataTarget}
                className={
                    props.disabled
                        ? (props.owner ? 'btn btn-primary disabled' : 'btn btn-success disabled')
                        : (props.owner ? 'btn btn-primary' : 'btn btn-success')
                }>
            {props.owner ? props.ownerTitle : props.userTitle}
            {props.owner ? (null) : (
                <span className="badge badge-dark ml-1">
                    {props.price}<i className="fa fa-rub ml-1"/>
                </span>
            )}
        </button>
    );
}