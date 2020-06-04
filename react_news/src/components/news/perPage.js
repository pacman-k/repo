import React from 'react';
import './perPage.css'

const PerPage = (props) =>(
        <div>
            <button className='p-button' style={(props.current == 5) ? { backgroundColor: 'rgb(56, 170, 65)' } : {}} value={5} onClick={props.change}>5</button>
            <button className='p-button' style={(props.current == 10) ? { backgroundColor: 'rgb(56, 170, 65)' } : {}}  value={10} onClick={props.change} >10</button>
            <button className='p-button' style={(props.current == 20) ? { backgroundColor: 'rgb(56, 170, 65)' } : {}} value={20} onClick={props.change}>20</button>
            <button className='p-button' style={(props.current == 50) ? { backgroundColor: 'rgb(56, 170, 65)' } : {}} value={50} onClick={props.change}>50</button>
        </div>
    );



export default PerPage;