import React from 'react';
import {Link} from 'react-router-dom';
import StarRate from '../../util/components/StarRate';
import placeimg from '../../util/img/no-image-white-sm.jpg';

export default function PlaygroundPageableContainer(props) {
    const page = props.page;
    const totalPages = (page != null) ? page.totalPages : 0;
    const content = (page != null) ? page.content : null;
    return ((content !== null) && (content.length > 0)) ? (
        <div className="PlaygroundPageableContainer col-xs-12 col-sm-8">
            <PlaygroundContainer content={content}/>
            {(totalPages > 1) ? (<PlaygroundPagination/>) : (null)}
        </div>
    ) : (((content !== null) && (content.length === 0)) ? (
        <div className="PlaygroundPageableContainer col-xs-12 col-sm-8">
            <div className="col-xs-12 col-sm-12 mb-12">
                <div className="alert alert-light">
                    <h4 className="alert-heading">Ничего не найдено!</h4>
                    <hr/>
                    <p className="mb-0">Не существует таких площадок, которые удовлетворяли бы запросу.</p>
                </div>
            </div>
        </div>
    ) : (
        <div className="PlaygroundPageableContainer col-xs-12 col-sm-8"/>
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
        content.forEach((value, index) => {
            container.push(<PlaygroundCard key={index} playground={value}/>);
        });
    }
    return (<div className="PlaygroundContainer row">{container}</div>);
}

function PlaygroundCard(props) {
    const playground = props.playground;
    const photoURLs = playground.photoURLs;
    const photoURL = ((photoURLs.length > 0) ? (photoURLs[0] + '?size=sm') : placeimg);
    return (
        <div className="PlaygroundCard col-xs-12 col-sm-6 mb-4">
            <div className="card">
                <img className="card-img" src={photoURL} alt={playground.name}
                     onError={(e) => {
                         e.target.onError = null;
                         e.target.src = placeimg
                     }}/>
                <div className="card-body">
                    <h4 className="card-title mb-1">
                        <small>{playground.name}</small>
                    </h4>
                    <h6 className="card-title mb-1">
                        <StarRate value={playground.rate}/>
                    </h6>
                    <p className="card-text">
                        <span className="badge badge-dark">
                            от<span className="mx-1">{Math.floor(playground.price)}</span><i className="fa fa-rub"/>/час
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