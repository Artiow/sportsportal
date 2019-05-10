import React from 'react';
import {Link} from 'react-router-dom';
import StarRate from '../../../util/components/StarRate';
import PlacedImg from '../../../util/components/PlacedImg';
import placeimg from '../../../util/img/no-image-white-sm.jpg';
import LoadingSpinner from '../../../util/components/LoadingSpinner';
import './PlaygroundPageableContainer.css'

export default function PlaygroundPageableContainer(props) {
    const page = props.content;
    const loading = props.loading;
    const totalPages = (page != null) ? page.totalPages : 0;
    const content = (page != null) ? page.content : null;
    return ((!!content) && (content.length > 0)) ? (
        <div className="PlaygroundPageableContainer col-xs-12 col-sm-8">
            <PlaygroundContainer content={content} disabled={loading}/>
            {(totalPages > 1) ? (<PlaygroundPagination/>) : (null)}
        </div>
    ) : (
        <div className="PlaygroundPageableContainer col-xs-12 col-sm-8">
            {(!content || loading) ? (
                <LoadingSpinner className="text-secondary mt-5"/>
            ) : (
                <div className="col-xs-12 col-sm-12 mb-12">
                    <div className="alert alert-light">
                        <h4 className="alert-heading">Ничего не найдено!</h4>
                        <hr/>
                        <p className="mb-0">Не существует таких площадок, которые удовлетворяли бы запросу.</p>
                    </div>
                </div>
            )}
        </div>
    );
}

function PlaygroundContainer(props) {
    let container = [];
    const disabled = props.disabled;
    const content = props.content;
    if ((content !== null) && (content.length > 0)) {
        content.forEach((value, index) => {
            container.push(<PlaygroundCard key={index} playground={value} disabled={disabled}/>);
        });
    }
    return (<div className="PlaygroundContainer row">{container}</div>);
}

function PlaygroundCard(props) {
    const disabled = props.disabled;
    const playground = props.playground;
    const photoURLs = playground.photoURLs;
    const photoURL = ((photoURLs.length > 0) ? (photoURLs[0] + '?size=sm') : placeimg);
    return (
        <div className="PlaygroundCard col-xs-12 col-sm-6 mb-4">
            <div className="card">
                <PlacedImg className="card-img" src={photoURL} placeImg={placeimg} alt={playground.name}
                           disabled={disabled}/>
                <div className="card-body">
                    <h4 className={'card-title mb-1' + (disabled ? ' text-muted' : '')}>
                        <small>{playground.name}</small>
                    </h4>
                    <h6 className={'card-title mb-1' + (disabled ? ' text-muted' : '')}>
                        <StarRate value={playground.rate}/>
                    </h6>
                    <p className="card-text">
                        {(!playground.isFreed) ? (
                            <span className={'badge badge-dark' + (disabled ? ' text-muted' : '')}>
                            от<span className="mx-1">{Math.floor(playground.price)}</span><i className="fa fa-rub"/>/час
                            </span>
                        ) : (
                            <span className={'badge badge-success' + (disabled ? ' text-muted' : '')}>
                            открытая<span className="mx-1">{Math.floor(playground.price)}</span><i className="fa fa-rub"/>/час
                            </span>
                        )}
                    </p>
                    <Link className={'btn btn-outline-info btn-sm' + (disabled ? ' disabled' : '')}
                          to={"/playground/id" + playground.id}>
                        Подробнее...
                    </Link>
                </div>
            </div>
        </div>
    );
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