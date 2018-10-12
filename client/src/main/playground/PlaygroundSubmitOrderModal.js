import React from 'react';
import {env} from '../../boot/constants';
import './PlaygroundSubmitOrderModal.css';

export default class PlaygroundSubmitOrderModal extends React.Component {

    static HEADER_TITLE = 'Подтвердите правильность выбора';
    static MAX_LENGTH = 7;

    static calcSchedule = (reservation) => {
        const rawSchedule = new Map();
        reservation.forEach(value => {
            const datetime = value.split('T');
            const date = datetime[0];
            const time = datetime[1];
            if (rawSchedule.has(date)) rawSchedule.get(date).push(time);
            else rawSchedule.set(date, [time]);
        });
        rawSchedule.forEach(value => value.sort());
        return Array.from(rawSchedule).sort();
    };

    constructor(props) {
        super(props);
        this.state = {
            schedule: PlaygroundSubmitOrderModal.calcSchedule(this.props.reservation),
            offset: 0
        }
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
            schedule: PlaygroundSubmitOrderModal.calcSchedule(nextProps.reservation),
            offset: 0
        });
    }

    render() {
        let maxHeight = 0;
        const headerLine = [];
        const schedule = this.state.schedule;
        schedule.forEach((value, index) => {
            const currentHeight = value[1].length;
            if (currentHeight > maxHeight) maxHeight = currentHeight;
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

        const body = [];
        for (let i = 0; i < maxHeight; i++) {
            const row = [];
            schedule.forEach((value, index) => row.push(<td key={index}>{value[1][i]}</td>));
            body.push(<tr key={i}>{row}</tr>);
        }

        return (
            <div id={this.props.submitId} className="PlaygroundSubmitOrderModal modal fade" tabIndex="-1">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title">{PlaygroundSubmitOrderModal.HEADER_TITLE}</h5>
                            <button type="button" className="close" data-dismiss="modal"><span>&times;</span></button>
                        </div>
                        <div className="modal-body">
                            <table className="table table-hover">
                                <thead className="thead-light">
                                <tr>{headerLine}</tr>
                                </thead>
                                <tbody>{body}</tbody>
                            </table>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-dismiss="modal">
                                {this.props.closeTitle}
                            </button>
                            <button type="submit" className={this.props.owner ? 'btn btn-primary' : 'btn btn-success'}>
                                {this.props.owner ? this.props.ownerTitle : this.props.userTitle}
                                {this.props.owner ? (null) : (
                                    <span className="badge badge-dark ml-1">
                                    {this.props.price}<i className="fa fa-rub ml-1"/>
                                </span>
                                )}
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}