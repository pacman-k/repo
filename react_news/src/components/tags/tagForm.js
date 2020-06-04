import React from 'react';
import i18next from 'i18next';
import { RefreshIndicator } from 'material-ui';

const TagForm = (props) => (
    <div style={{ width: '40%' }}>
        <form onSubmit={props.save}>
            <div className="form-group input-group">
                <input maxLength={30} required type="text" className="form-control" id="name" placeholder="Enter Name" />
            </div>
            <div className="input-group">
                <button type="submit" className="btn btn-primary">{i18next.t('submit')}</button>
                <button type="reset" className="btn btn-danger" style={{marginLeft: "10px"}}>reset</button>
            </div>
        </form>
    </div>

);

export default TagForm;