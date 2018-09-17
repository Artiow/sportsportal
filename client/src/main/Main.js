import React, {Component} from "react";
import {Link} from 'react-router-dom';
import {getApiUrl} from '../boot/constants'
import Slider, {Range} from 'rc-slider';
import 'rc-slider/assets/index.css';
import axios from 'axios';
import './Main.css';
import noImageSm from '../util/img/no-image-sm.jpg';

class Main extends Component {

    static PAGE_SIZE = 10;

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
                pageNum: 0,
                pageSize: Main.PAGE_SIZE
            }
        };
        this.query();
    }

    query() {
        const self = this;
        const url = getApiUrl('/leaseapi/playground/list');
        axios.get(url, {params: this.state.filter})
            .then(function (response) {
                console.log('Response:', response);
                const data = response.data;
                self.setState({
                    content: data.content,
                    pageNumber: data.pageNumber,
                    totalPages: data.totalPages
                });
            })
            .catch(function (error) {
                console.log('Error:', error);
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

    /**
     * Load dictionary and store it in state.
     * @param uri {string}
     * @param setting {function(object)}
     */
    uploadFilerData(uri, setting) {
        axios.get(getApiUrl(uri))
            .then(function (response) {
                console.log('API Response:', response);
                setting(response.data.content);
            })
            .catch(function (error) {
                console.log('API Error:', error);
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
            startCost: this.state.startCost,
            endCost: this.state.endCost,
            opening: this.state.opening,
            closing: this.state.closing,
            minRate: this.state.minRate
        })
    }

    render() {
        const setFilterData = (prefix, content) => {
            const result = [];
            const handler = event => {
                console.log(event.target);
            };
            if ((content != null) && (content.length > 0)) {
                content.forEach(function (item, i, arr) {
                    const id = prefix + '_' + item.code;
                    const name = item.name.charAt(0).toUpperCase() + item.name.slice(1);
                    result.push(
                        <div key={i} className="custom-control custom-checkbox">
                            <input type="checkbox" className="custom-control-input" onChange={handler} id={id}/>
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
                                    {setFilterData('sport', this.state.sports)}
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
                                    {setFilterData('feature', this.state.features)}
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
                                                <Rate rate={this.state.minRate}/>
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
    if ((content !== null) && (content.length > 0)) {
        return (
            <div className="PageablePlaygroundContainer col-xs-12 col-sm-8">
                <PlaygroundContainer content={content}/>
                {(pageable) ? (<PlaygroundPagination/>) : (null)}
            </div>
        );
    } else {
        return (
            <div className="PageablePlaygroundContainer col-xs-12 col-sm-8">
                <div className="col-xs-12 col-sm-12 mb-12">
                    <div className="alert alert-primary">Ничего не найдено!</div>
                </div>
            </div>
        );
    }
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
    const content = props.content;
    let container = [];
    if (content.length > 0) {
        content.forEach(function (item, i, arr) {
            container.push(<PlaygroundCard key={i} playground={item}/>);
        });
    } else {
        container.push(
            <div className="col-xs-12 col-sm-12 mb-12">
                <div className="alert alert-primary">Ничего не найдено!</div>
            </div>
        );
    }
    return (<div className="PlaygroundContainer row">{container}</div>);
}

function PlaygroundCard(props) {
    const playground = props.playground;
    const photoURLs = playground.photoURLs;
    const photoURL = ((photoURLs.length > 0) ? (photoURLs[0] + '?size=sm') : noImageSm);
    return (
        <div className="PlaygroundCard col-xs-12 col-sm-6 mb-4">
            <div className="card">
                <img className="card-img" src={photoURL} alt={playground.name}/>
                <div className="card-body">
                    <h4 className="card-title">
                        <small>{playground.name}</small>
                    </h4>
                    <h6 className="card-title">
                        <Rate rate={playground.rate}/>
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

function Rate(props) {
    const rate = props.rate;
    let stars = [];

    let i = 0;
    while (i <= (rate - 2)) {
        stars.push(<i key={i} className="fa fa-star"/>);
        i += 2;
    }
    if (i < rate) {
        stars.push(<i key={i} className="fa fa-star-half-o"/>);
        i += 2;
    }
    while (i < 10) {
        stars.push(<i key={i} className="fa fa-star-o"/>);
        i += 2;
    }

    return (
        <span className="Rate card-title">{stars}</span>
    );
}

export default Main;