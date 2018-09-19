import React from 'react';
import './NoMatch.css';

function NoMatch() {
    return (
        <div className="NoMatch page-wrap d-flex flex-row align-items-center">
            <div className="container">
                <div className="row justify-content-center">
                    <div className="col-md-12 text-center">
                        <span className="d-block display-1">404</span>
                        <div className="mb-4 lead">Страница, которую вы ищите, не найдена.</div>
                        <a href="/" className="btn btn-link">На домашнюю страницу</a>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default NoMatch;