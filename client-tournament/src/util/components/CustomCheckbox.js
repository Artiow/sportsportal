import React from 'react';

/**
 * CustomCheckbox by bootstrap custom-control.
 * @author Artem Namednev <namednev.artem@gmail.com>
 * @param props {object} component props
 * @return carousel component
 */
export default function CustomCheckbox(props) {
    return (
        <div className="CustomCheckbox custom-control custom-checkbox">
            <input id={props.id}
                   type="checkbox"
                   value={props.value}
                   className="custom-control-input"
                   defaultChecked={props.defaultChecked}
                   onChange={props.onChange}
                   disabled={props.disabled}/>
            <label className="custom-control-label"
                   htmlFor={props.id}>
                {props.children}
            </label>
        </div>
    )
}