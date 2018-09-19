import React, {Component} from "react";
import Slider, {Range} from 'rc-slider';
import 'rc-slider/assets/index.css';
import {Link} from 'react-router-dom';
import {getApiUrl} from '../boot/constants'
import StarRate from '../util/components/StarRate';
import axios from 'axios';
import qs from 'qs';
import './Index.css';
import noImage from '../util/img/no-image-white-sm.jpg';

class Index extends Component {

    static DEFAULT_PAGE_SIZE = 10;

    updateFilterCallback = newFilter => {
        this.setState(prevState => {
            return {filter: Object.assign(prevState.filter, newFilter)}
        });
        this.query();
    };

    constructor(props) {
        super(props);
        this.state = {
            content: null,
            pageNumber: 0,
            totalPages: 0,
            filter: {
                pageSize: Index.DEFAULT_PAGE_SIZE,
                pageNum: 0
            }
        };
        this.query();
    }

    query() {
        const self = this;
        const url = getApiUrl('/leaseapi/playground/list');
        axios.get(url, {
            params: this.state.filter,
            paramsSerializer: params => {
                return qs.stringify(params, {arrayFormat: 'repeat'})
            }
        }).then(function (response) {
            console.log('Query Response:', response);
            const data = response.data;
            self.setState({
                content: data.content,
                pageNumber: data.pageNumber,
                totalPages: data.totalPages
            });
        }).catch(function (error) {
            console.log('Query Error:', error);
        })
    }

    render() {
        return (
            <main className="Main container">
                <div className="row">
                    <PlaygroundFilter callback={this.updateFilterCallback}/>
                    <PageablePlaygroundContainer pageable={(this.state.totalPages > 1)}
                                                 content={this.state.content}/>
                </div>
            </main>
        );
    }
}

class PlaygroundFilter extends Component {

    static MIN_COST = 0;
    static MAX_COST = 1000000;

    updateRateCallback = slider => {
        this.setState({
            minRate: slider
        });
    };

    updateCostCallback = range => {
        const MAX = PlaygroundFilter.MAX_COST;
        this.setState({
            startCost: (MAX * (range[0] / 100)),
            endCost: (MAX * (range[1] / 100))
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
        this.state = {
            sports: null,
            sportCodes: [],
            features: null,
            featureCodes: [],
            searchString: '',
            startCost: PlaygroundFilter.MIN_COST,
            endCost: PlaygroundFilter.MAX_COST,
            opening: '00:00',
            closing: '00:00',
            minRate: 0
        };

        const self = this;
        this.uploadFilerData("/leaseapi/dict/feature/list", function (list) {
            self.setState({features: list});
        });
        this.uploadFilerData("/leaseapi/dict/sport/list", function (list) {
            self.setState({sports: list});
        });
    }

    static updateCodeArray(codes, code, checked) {
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
        axios.get(getApiUrl(uri))
            .then(function (response) {
                console.log('Dictionary Response:', response);
                setting(response.data.content);
            })
            .catch(function (error) {
                console.log('Dictionary Error:', error);
            })
    }

    handleInputChange(event) {
        this.setState({
            searchString: event.target.value
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        this.props.callback({
            searchString: this.state.searchString,
            featureCodes: this.state.featureCodes,
            sportCodes: this.state.sportCodes,
            startCost: this.state.startCost,
            endCost: this.state.endCost,
            opening: this.state.opening,
            closing: this.state.closing,
            minRate: this.state.minRate
        })
    }

    updateSportCodes(code, checked) {
        this.setState(prevState => {
            return {sportCodes: PlaygroundFilter.updateCodeArray(prevState.sportCodes, code, checked)}
        });
    }

    updateFeatureCodes(code, checked) {
        this.setState(prevState => {
            return {featureCodes: PlaygroundFilter.updateCodeArray(prevState.featureCodes, code, checked)}
        });
    }

    render() {
        const setFilterData = (prefix, content, updater) => {
            const result = [];
            if ((content != null) && (content.length > 0)) {
                content.forEach(function (item, i, arr) {
                    const code = item.code;
                    const id = prefix + '_' + code;
                    const name = item.name.charAt(0).toUpperCase() + item.name.slice(1);
                    result.push(
                        <div key={i} className="custom-control custom-checkbox">
                            <input type="checkbox" className="custom-control-input"
                                   value={code} id={id}
                                   onChange={event => {
                                       updater(event.target.value, event.target.checked);
                                   }}/>
                            <label className="custom-control-label" htmlFor={id}>{name}</label>
                        </div>
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
                    <div id="accordion">
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className="btn btn-link" data-toggle="collapse" data-target="#collapse_1">
                                        Виды спорта
                                    </a>
                                </h5>
                            </div>
                            <div id="collapse_1" className="collapse" data-parent="#accordion">
                                <div className="card-body">
                                    {setFilterData('sport', this.state.sports, this.updateSportCodes.bind(this))}
                                </div>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className="btn btn-link" data-toggle="collapse" data-target="#collapse_2">
                                        Инфраструктура
                                    </a>
                                </h5>
                            </div>
                            <div id="collapse_2" className="collapse" data-parent="#accordion">
                                <div className="card-body">
                                    {setFilterData('feature', this.state.features, this.updateFeatureCodes.bind(this))}
                                </div>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className="btn btn-link" data-toggle="collapse" data-target="#collapse_3">
                                        Стоимость часа
                                    </a>
                                </h5>
                            </div>
                            <div id="collapse_3" className="collapse" data-parent="#accordion">
                                <div className="card-body">
                                    <h6>
                                        <span className="badge-sub">от</span>
                                        <span className="badge badge-dark">
                                            <span className="badge-param">
                                                {(this.state.startCost / 100).toFixed()}
                                            </span>
                                            <i className="fa fa-rub"/>/час
                                        </span>
                                    </h6>
                                    <h6>
                                        <span className="badge-sub">до</span>
                                        <span className="badge badge-dark">
                                            <span className="badge-param">
                                                {(this.state.endCost / 100).toFixed()}
                                            </span>
                                            <i className="fa fa-rub"/>/час
                                        </span>
                                    </h6>
                                    <Range allowCross={false} defaultValue={[0, 100]}
                                           onChange={this.updateCostCallback}/>
                                </div>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className="btn btn-link" data-toggle="collapse" data-target="#collapse_4">
                                        Время работы
                                    </a>
                                </h5>
                            </div>
                            <div id="collapse_4" className="collapse" data-parent="#accordion">
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
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className="btn btn-link" data-toggle="collapse" data-target="#collapse_5">
                                        Рейтинг
                                    </a>
                                </h5>
                            </div>
                            <div id="collapse_5" className="collapse" data-parent="#accordion">
                                <div className="card-body">
                                    <h6 className="badge badge-dark">
                                            <span className="badge-param">
                                                <StarRate value={this.state.minRate}/>
                                            </span>
                                    </h6>
                                    <Slider min={0} max={10} defaultValue={0}
                                            onChange={this.updateRateCallback}/>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        );
    }
}

function PageablePlaygroundContainer(props) {
    const content = props.content;
    const pageable = props.pageable;
    return ((content !== null) && (content.length > 0)) ? (
        <div className="PageablePlaygroundContainer col-xs-12 col-sm-8">
            <PlaygroundContainer content={content}/>
            {(pageable) ? (<PlaygroundPagination/>) : (null)}
        </div>
    ) : (((content !== null) && (content.length === 0)) ? (
        <div className="PageablePlaygroundContainer col-xs-12 col-sm-8">
            <div className="col-xs-12 col-sm-12 mb-12">
                <div className="alert alert-light">
                    <h4 className="alert-heading">Ничего не найдено!</h4>
                    <hr/>
                    <p className="mb-0">Не существует таких площадок, которые удовлетворяли бы запросу.</p>
                </div>
            </div>
        </div>
    ) : (
        <div className="PageablePlaygroundContainer col-xs-12 col-sm-8"/>
    ));
}

function PlaygroundPagination(props) {
    return (
        <div className="PlaygroundPagination row">
            <div className="col-xs-12 col-sm-12 mb-4">
                <div className="card" style={{minHeight: '50px'}}/>
            </div>
        </div>
    );
}

function PlaygroundContainer(props) {
    let container = [];
    const content = props.content;
    if ((content !== null) && (content.length > 0)) {
        content.forEach(function (item, i, arr) {
            container.push(<PlaygroundCard key={i} playground={item}/>);
        });
    }
    return (<div className="PlaygroundContainer row">{container}</div>);
}

function PlaygroundCard(props) {
    const playground = props.playground;
    const photoURLs = playground.photoURLs;
    const photoURL = ((photoURLs.length > 0) ? (photoURLs[0] + '?size=sm') : noImage);
    return (
        <div className="PlaygroundCard col-xs-12 col-sm-6 mb-4">
            <div className="card">
                <img className="card-img" src={photoURL} alt={playground.name}/>
                <div className="card-body">
                    <h4 className="card-title">
                        <small>{playground.name}</small>
                    </h4>
                    <h6 className="card-title">
                        <StarRate value={playground.rate}/>
                    </h6>
                    <p className="card-text">
                        <span className="badge badge-dark">
                            от<span>{((playground.cost / 100).toFixed())}</span><i className="fa fa-rub"/>/час
                        </span>
                    </p>
                    <Link to={"/playground/id" + playground.id} className="btn btn-outline-info btn-sm">
                        Подробнее...
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default Index;