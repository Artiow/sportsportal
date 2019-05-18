import React from 'react';
import {Range} from 'rc-slider';
import Dictionary from '../../../connector/Dictionary';
import 'rc-slider/assets/index.css';
import CustomCheckbox from '../../../util/components/CustomCheckbox';
import './PlaygroundFilter.css';

export default class PlaygroundFilter extends React.Component {

    static MIN_PRICE = 0;
    static MAX_PRICE = 10000;
    static PRICE_STEP = 100;

    static MIN_TIME = '00:00';
    static MAX_TIME = '00:00';
    static MIN_NUMERIC_TIME = 0;
    static MAX_NUMERIC_TIME = 48;
    static TIME_STEP = 1;


    constructor(props) {
        super(props);
        this.dictionary = {
            sports: null,
            features: null
        };
        this.state = {
            loading: true,
            sportCodes: [],
            featureCodes: [],
            searchString: '',
            includeFreed: true,
            includeBooked: false,
            minPrice: PlaygroundFilter.MIN_PRICE,
            maxPrice: PlaygroundFilter.MAX_PRICE,
            opening: PlaygroundFilter.MIN_TIME,
            closing: PlaygroundFilter.MAX_TIME
        };
    }


    static updateCheckboxArray(codes, code, checked) {
        const idx = codes.indexOf(code);
        if ((checked) && (idx < 0)) codes.push(code);
        else codes.splice(idx, 1);
        return codes;
    }


    updatePriceCallback = range => {
        this.setState({
            minPrice: range[0],
            maxPrice: range[1]
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


    componentDidMount() {
        this.uploadFilterDictionaryData('feature', 'features');
        this.uploadFilterDictionaryData('sport', 'sports');
        this.handleSubmit();
    }

    /**
     * Load dictionary and store it in state.
     * @param name {string}
     * @param dictionary {string}
     */
    uploadFilterDictionaryData(name, dictionary) {
        Dictionary.list(name)
            .then(data => {
                console.debug('PlaygroundFilter', `query:${dictionary}`, data);
                this.dictionary[dictionary] = data.content;
                this.updateLoadingStatus();
            })
            .catch(error => {
                console.error('PlaygroundFilter', `query:${dictionary}`, error);
            });
    }

    updateLoadingStatus() {
        this.setState({loading: !this.dictionary.features && !this.dictionary.sports});
    }

    handleInputChange(event) {
        this.setState({searchString: event.target.value});
    }

    handleSubmit(event) {
        if (event) event.preventDefault();
        const onChange = this.props.onChange;
        if (typeof onChange === 'function') onChange({
            searchString: this.state.searchString,
            featureCodes: this.state.featureCodes,
            sportCodes: this.state.sportCodes,
            includeFreed: this.state.includeFreed,
            includeBooked: this.state.includeBooked,
            minPrice: this.state.minPrice,
            maxPrice: this.state.maxPrice,
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
        const loading = this.state.loading;
        const inProcess = this.props.inProcess;
        const setCheckboxData = (prefix, content, updater) => {
            const result = [];
            if ((content != null) && (content.length > 0)) {
                content.forEach((value, index) => {
                    result.push(
                        <CustomCheckbox key={index} id={prefix + '_' + value.code} value={value.code}
                                        onChange={event => updater(event.target.value, event.target.checked)}>
                            {value.name.charAt(0).toUpperCase() + value.name.slice(1)}
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
                               type="text" className="form-control" disabled={loading}/>
                        <div className="input-group-append">
                            <button
                                className={'btn btn-outline-secondary' + ((loading || inProcess) ? ' disabled' : '')}
                                disabled={(loading || inProcess)} type="submit">
                                {(!inProcess) ? (
                                    <span>Найти</span>
                                ) : (
                                    <i className="fa fa-refresh fa-spin fa-fw"/>
                                )}
                            </button>
                        </div>
                    </div>
                    <div id="pg_filter_accordion" className="filter-accordion">
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className={'btn btn-link' + (loading ? ' disabled' : '')}
                                       data-toggle="collapse" data-target="#pg_filter_collapse_1">
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
                                    <a className={'btn btn-link' + (loading ? ' disabled' : '')}
                                       data-toggle="collapse" data-target="#pg_filter_collapse_2">
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
                                    <a className={'btn btn-link' + (loading ? ' disabled' : '')}
                                       data-toggle="collapse" data-target="#pg_filter_collapse_3">
                                        Стоимость часа аренды
                                    </a>
                                </h5>
                            </div>
                            <div id="pg_filter_collapse_3" className="collapse" data-parent="#pg_filter_accordion">
                                <div className="card-body">
                                    <CustomCheckbox id={'include_freed'}
                                                    defaultChecked={this.state.includeFreed}
                                                    onChange={e => this.setState({includeFreed: e.target.checked})}
                                                    disabled={true} /* choice disabling */>
                                        Общественные
                                    </CustomCheckbox>
                                    <CustomCheckbox id={'include_booked'}
                                                    defaultChecked={this.state.includeBooked}
                                                    onChange={e => this.setState({includeBooked: e.target.checked})}
                                                    disabled={true} /* choice disabling */>
                                        Арендуемые
                                    </CustomCheckbox>
                                    <hr className="card-liner"/>
                                    <h6>
                                        <span className={`badge-sub ${!this.state.includeBooked ? 'text-muted' : ''}`}>
                                            от
                                        </span>
                                        <span
                                            className={`badge badge-${this.state.includeBooked ? 'dark' : 'secondary'}`}>
                                            <span className="badge-param">
                                                {Math.floor(this.state.minPrice)}
                                            </span>
                                            <i className="fa fa-rub"/>/час
                                        </span>
                                    </h6>
                                    <h6>
                                        <span className={`badge-sub ${!this.state.includeBooked ? 'text-muted' : ''}`}>
                                            до
                                        </span>
                                        <span
                                            className={`badge badge-${this.state.includeBooked ? 'dark' : 'secondary'}`}>
                                            <span className="badge-param">
                                                {Math.floor(this.state.maxPrice)}
                                            </span>
                                            <i className="fa fa-rub"/>/час
                                        </span>
                                    </h6>
                                    <Range allowCross={false}
                                           min={PlaygroundFilter.MIN_PRICE}
                                           max={PlaygroundFilter.MAX_PRICE}
                                           defaultValue={[PlaygroundFilter.MIN_PRICE, PlaygroundFilter.MAX_PRICE]}
                                           step={PlaygroundFilter.PRICE_STEP}
                                           onChange={this.updatePriceCallback}
                                           disabled={!this.state.includeBooked}/>
                                </div>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className={'btn btn-link' + (loading ? ' disabled' : '')}
                                       data-toggle="collapse" data-target="#pg_filter_collapse_4">
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
                                    <Range allowCross={false}
                                           min={PlaygroundFilter.MIN_NUMERIC_TIME}
                                           max={PlaygroundFilter.MAX_NUMERIC_TIME}
                                           defaultValue={[PlaygroundFilter.MIN_NUMERIC_TIME, PlaygroundFilter.MAX_NUMERIC_TIME]}
                                           step={PlaygroundFilter.TIME_STEP}
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