import React from 'react';
import {env} from '../../boot/constants';
import './PlaygroundSubmitOrderModal.css';

export default function PlaygroundSubmitOrderModal(props) {

    const HEADER_TITLE = 'Подтвердите правильность выбора';

    const rawSchedule = new Map();
    const a = [];
    props.reservation.forEach(value => {
        const datetime = value.split('T');
        const date = datetime[0];
        const time = datetime[1];
        if (rawSchedule.has(date)) rawSchedule.get(date).push(time);
        else rawSchedule.set(date, [time]);
    });

    const table = [];
    const headerLine = [];
    rawSchedule.forEach(value => value.sort());
    const schedule = Array.from(rawSchedule).sort();
    schedule.forEach((value, index) => {
        const date = value[0];
        const dateClass = new Date(date);
        headerLine.push(
            <th key={index} className="th-calendar">
                <div className="small">{env.MONTH_NAMES[dateClass.getMonth()]}</div>
                <div>
                    <span className="mr-1">{date.split('-')[2]}</span>
                    <span className="ml-1">{env.DAYS_OF_WEEK_NAMES[dateClass.getDay()].short.toUpperCase()}</span>
                </div>
            </th>
        );
    });

    return (
        <div id={props.submitId} className="PlaygroundSubmitOrderModal modal fade" tabIndex="-1">
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title">{HEADER_TITLE}</h5>
                        <button type="button" className="close" data-dismiss="modal"><span>&times;</span></button>
                    </div>
                    <div className="modal-body">
                        <table className="table table-hover">
                            <thead className="thead-light">
                            <tr>{headerLine}</tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" data-dismiss="modal">
                            {props.closeTitle}
                        </button>
                        <button type="submit" className={props.owner ? 'btn btn-primary' : 'btn btn-success'}>
                            {props.owner ? props.ownerTitle : props.userTitle}
                            {props.owner ? (null) : (
                                <span className="badge badge-dark ml-1">
                                    {props.price}<i className="fa fa-rub ml-1"/>
                                </span>
                            )}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}