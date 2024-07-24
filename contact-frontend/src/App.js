import './App.css';
import { useEffect, useState } from 'react';
import { getAllContacts } from './api/ContactService';
import { Routes, Route, Navigate } from 'react-router-dom';

// components
import Header from './components/Header';
import ContactList from './components/ContactList';

function App() {

  const [data, setData] = useState({});
  const [currentPage, setCurrentPage] = useState(0);

  const getContacts = async (page = 0, size = 10) => {
    try {
      setCurrentPage(page);
      const { data } = await getAllContacts(page, size);
      setData(data);
      console.log(data);
    } catch (err) {
      console.log(err);
    }
  }

  useEffect(() => {
    getContacts();
  }, []);

  const toggleModal = (show) => {
    console.log('clicked!');
  }

  return (
    <>
      <Header toggleModal={toggleModal} nbOfContacts={data.totalElements} />
      <main className='main'>
        <div className='container'>
          <Routes>
            <Route path='/' element={<Navigate to={'/contacts'} />} />
            <Route path='/contacts' element={<ContactList data={data} currentPage={currentPage} getAllContacts={getContacts} />} />
          </Routes>
        </div>
      </main>
    </>
  );
}

export default App;
