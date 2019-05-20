import React from 'react';
import {env} from '../../constants';
import './Footer.css';

export default function Footer(props) {
    return (
        <footer className="Footer">
            <div className="container">
                <div className="row">
                    <div className="col-md-6">
                        <a href={env.API_HOST + '/swagger-ui.html'}>
                            <h6>
                                <i className="fa fa-cogs"/>
                                <small>SportsPortal API Documentation</small>
                            </h6>
                        </a>
                    </div>
                </div>
            </div>
        </footer>
    );
}