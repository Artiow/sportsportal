import React from 'react';
import {Range} from 'rc-slider';
import 'rc-slider/assets/index.css';
import CustomCheckbox from '../../util/components/CustomCheckbox';
import apiUrl from '../../boot/constants';
import axios from 'axios';
import './PlaygroundFilter.css';

export default class PlaygroundFilter extends React.Component {

    static MIN_PRICE = 0;
    static MAX_PRICE = 10000;
    static PRICE_STEP = 100;

    updatePriceCallback = range => {
        this.setState({
            startPrice: range[0],
            endPrice: range[1]
        });
    };

    updateTimeCallback = range => {
        const normalize = time => {
            const normalTime = (time !== 48) ? time : 0;
            const timeHour = Math.floor(normalTime / 2);
            const timeMinute = (30 * (normalTime % 2));
            return ((timeHour < 10) ? ('0' + timeHour) : timeHour) + ':' + ((timeMinute !== 30) ? (timeMinute + '0') : timeMinute)
        };
        this.setState({
            opening: normalize(range[0]),
            closing: normalize(range[1])
        });
    };

    constructor(props) {
        super(props);
        this.dictionary = {sports: null, features: null};
        this.uploadFilerData('/leaseapi/dict/feature/list', response => this.dictionary.features = response);
        this.uploadFilerData('/leaseapi/dict/sport/list', response => this.dictionary.sports = response);
        this.state = {
            sportCodes: [],
            featureCodes: [],
            searchString: '',
            startPrice: PlaygroundFilter.MIN_PRICE,
            endPrice: PlaygroundFilter.MAX_PRICE,
            opening: '00:00',
            closing: '00:00'
        };
    }

    static updateCheckboxArray(codes, code, checked) {
        const idx = codes.indexOf(code);
        if ((checked) && (idx < 0)) codes.push(code);
        else codes.splice(idx, 1);
        return codes;
    }

    /**
     * Load dictionary and store it in state.
     * @param uri {string}
     * @param setting {function(object)}
     */
    uploadFilerData(uri, setting) {
        axios.get(apiUrl(uri))
            .then(function (response) {
                console.log('Dictionary Response:', response);
                setting(response.data.content);
            })
            .catch(function (error) {
                console.log('Dictionary Error:', ((error.response != null) ? error.response : error));
            })
    }

    handleInputChange(event) {
        this.setState({
            searchString: event.target.value
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        const onChange = this.props.onChange;
        if (typeof onChange === 'function') onChange({
            searchString: this.state.searchString,
            featureCodes: this.state.featureCodes,
            sportCodes: this.state.sportCodes,
            startPrice: this.state.startPrice,
            endPrice: this.state.endPrice,
            opening: this.state.opening,
            closing: this.state.closing
        });
    }

    updateSportCodes(code, checked) {
        this.setState(prevState => {
            return {sportCodes: PlaygroundFilter.updateCheckboxArray(prevState.sportCodes, code, checked)}
        });
    }

    updateFeatureCodes(code, checked) {
        this.setState(prevState => {
            return {featureCodes: PlaygroundFilter.updateCheckboxArray(prevState.featureCodes, code, checked)}
        });
    }

    render() {
        const setCheckboxData = (prefix, content, updater) => {
            const result = [];
            if ((content != null) && (content.length > 0)) {
                content.forEach(function (item, i, arr) {
                    result.push(
                        <CustomCheckbox key={i} id={prefix + '_' + item.code} value={item.code}
                                        onChange={event => updater(event.target.value, event.target.checked)}>
                            {item.name.charAt(0).toUpperCase() + item.name.slice(1)}
                        </CustomCheckbox>
                    );
                });
            }
            return result;
        };
        return (
            <div className="PlaygroundFilter col-xs-12 col-sm-4 mb-4">
                <form onSubmit={this.handleSubmit.bind(this)} className="card">
                    <div className="input-group mb-3">
                        <input id="searchString" onChange={this.handleInputChange.bind(this)}
                               type="text" className="form-control"/>
                        <div className="input-group-append">
                            <button className="btn btn-outline-secondary" type="submit">Найти</button>
                        </div>
                    </div>
                    <div id="pg_filter_accordion" className="filter-accordion">
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className="btn btn-link" data-toggle="collapse"
                                       data-target="#pg_filter_collapse_1">
                                        Виды спорта
                                    </a>
                                </h5>
                            </div>
                            <div id="pg_filter_collapse_1" className="collapse" data-parent="#pg_filter_accordion">
                                <div className="card-body">
                                    {setCheckboxData('sport', this.dictionary.sports, this.updateSportCodes.bind(this))}
                                </div>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className="btn btn-link" data-toggle="collapse"
                                       data-target="#pg_filter_collapse_2">
                                        Инфраструктура
                                    </a>
                                </h5>
                            </div>
                            <div id="pg_filter_collapse_2" className="collapse" data-parent="#pg_filter_accordion">
                                <div className="card-body">
                                    {setCheckboxData('feature', this.dictionary.features, this.updateFeatureCodes.bind(this))}
                                </div>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className="btn btn-link" data-toggle="collapse"
                                       data-target="#pg_filter_collapse_3">
                                        Стоимость часа
                                    </a>
                                </h5>
                            </div>
                            <div id="pg_filter_collapse_3" className="collapse" data-parent="#pg_filter_accordion">
                                <div className="card-body">
                                    <h6>
                                        <span className="badge-sub">от</span>
                                        <span className="badge badge-dark">
                                            <span className="badge-param">
                                                {Math.floor(this.state.startPrice)}
                                            </span>
                                            <i className="fa fa-rub"/>/час
                                        </span>
                                    </h6>
                                    <h6>
                                        <span className="badge-sub">до</span>
                                        <span className="badge badge-dark">
                                            <span className="badge-param">
                                                {Math.floor(this.state.endPrice)}
                                            </span>
                                            <i className="fa fa-rub"/>/час
                                        </span>
                                    </h6>
                                    <Range allowCross={false}
                                           min={PlaygroundFilter.MIN_PRICE} max={PlaygroundFilter.MAX_PRICE}
                                           defaultValue={[PlaygroundFilter.MIN_PRICE, PlaygroundFilter.MAX_PRICE]}
                                           step={PlaygroundFilter.PRICE_STEP}
                                           onChange={this.updatePriceCallback}/>
                                </div>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className="btn btn-link" data-toggle="collapse"
                                       data-target="#pg_filter_collapse_4">
                                        Время работы
                                    </a>
                                </h5>
                            </div>
                            <div id="pg_filter_collapse_4" className="collapse" data-parent="#pg_filter_accordion">
                                <div className="card-body">
                                    <h6>
                                        <span className="badge-sub">от</span>
                                        <span className="badge badge-dark">
                                            <span className="badge-param">{this.state.opening}</span>
                                        </span>
                                    </h6>
                                    <h6>
                                        <span className="badge-sub">до</span>
                                        <span className="badge badge-dark">
                                            <span className="badge-param">{this.state.closing}</span>
                                        </span>
                                    </h6>
                                    <Range min={0} max={48}
                                           allowCross={false} defaultValue={[0, 48]}
                                           onChange={this.updateTimeCallback}/>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        );
    }
}