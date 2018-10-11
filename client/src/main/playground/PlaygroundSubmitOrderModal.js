import React from 'react';

export default function PlaygroundSubmitOrderModal(props) {

    const HEADER_TITLE = 'Подтвердите правильность выбора';

    return (
        <div id={props.submitId} className="PlaygroundOrderModal modal fade" tabIndex="-1">
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title">{HEADER_TITLE}</h5>
                        <button type="button" className="close" data-dismiss="modal"><span>&times;</span></button>
                    </div>
                    <div className="modal-body">
                        ...
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" data-dismiss="modal">
                            {props.closeTitle}
                        </button>
                        <button type="submit" className={props.owner ? 'btn btn-primary' : 'btn btn-success'}>
                            {props.owner ? props.ownerTitle : props.userTitle}
                            {props.owner ? (null) : (
                                <span className="badge badge-dark ml-1">
                                    {props.price}<i className="fa fa-rub ml-1"/>
                                </span>
                            )}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}