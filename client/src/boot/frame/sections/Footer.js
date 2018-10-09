import React from 'react';
import {Link} from 'react-router-dom';
import apiUrl from '../../constants';
import './Footer.css';

export default function Footer(props) {
    return (
        <footer className="Footer">
            <div className="container">
                <div className="row">
                    <div className="col-md-6">
                        <h6><Link to={apiUrl('/')}>
                            <i className="fa fa-cogs"/>
                            <small>SportsPortal API</small>
                        </Link></h6>
                    </div>
                </div>
            </div>
        </footer>
    );
}