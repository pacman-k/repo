import React from 'react';
import Select from 'react-select';
import './selectSection.css';
import i18next from 'i18next';


var searchCriteria = {
    authorDtoName: "",
    authorDtoSurname: "",
    tags: []
}

const SelectSection = (props) => {

    const authorNameOptions = props.authors.map(author => (
         { value: author.name, label: author.name} )
    );

    const authorSurnameOptions = props.authors.map(author => (
     { value: author.surname, label: author.surname })
    );

    const tagsOptions = props.tags.map(tag => (
         { value: tag.name, label: tag.name }
    ));

    const onNameChange = selected => {
        if (!selected) {
            searchCriteria.authorDtoName = ""
            return
        }
        searchCriteria.authorDtoName = selected.value;
    }
    const onSurnameChange = selected => {
        if (!selected) {
            searchCriteria.authorDtoSurname = ""
            return
        }
        searchCriteria.authorDtoSurname = selected.value;
    }
    const onTagsChange = selected => {
        if (!selected) {
            searchCriteria.tags = []
            return
        }
        searchCriteria.tags = selected.map(option => ( option.value ))
    }

    const searchSubmit = () => {
        props.submit(searchCriteria);
    }
    const cancelSubmit = () => {
        document
        props.submit({});
    }

    return (


        <div>
            <Select isMulti onChange={onTagsChange} placeholder={i18next.t('tags')} options={tagsOptions} />
            <br />
            <Select isClearable defaultInputValue={searchCriteria.authorDtoName} onChange={onNameChange} className='author' placeholder={i18next.t('name')} options={authorNameOptions} />
            <Select isClearable defaultInputValue={searchCriteria.authorDtoSurname} onChange={onSurnameChange} className='author' placeholder={i18next.t('surname')} options={authorSurnameOptions} />
            <br />
            <br />
            <button className='btn btn-primary own-button' onClick={searchSubmit} type="button">{i18next.t('search')}</button>
            <button className='btn btn-danger own-button' onClick={cancelSubmit} type="button">{i18next.t('cancel')}</button>
        </div>



    );

}

export default SelectSection;

