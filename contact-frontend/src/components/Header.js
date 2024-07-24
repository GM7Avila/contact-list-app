import React from 'react'

const Header = ({ toggleModal, nbOfContacts }) => {
  return (
    <div>
      <header className='header'>
        <div className='container'>
          <h3>Contact List ( {nbOfContacts} )</h3>
          <button onClick = {() => toggleModal(true)} className='btn'>
            <i className='bi bi-plus-quare'></i> Add New Contact 
          </button>
        </div>
      </header>
    </div>
  )
}

export default Header
