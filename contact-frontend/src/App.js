import { useEffect, useState } from 'react';
import './App.css';
import { getAllContacts } from './api/ContactService';

function App() {

  const [data, setData] = useState({});
  const [currentPage, setCurrentPage] = useState(0);

  const getContacts = async(page = 0, size = 10) => {
    try {
      setCurrentPage(page);
      const { data } = await getAllContacts(page, size);
      setData(data);
      console.log(data);
    } catch(err) {
      console.log(err);
    }
  }

  useEffect(() => {
    getContacts();
  }, []);

  return (
    <div className="App">
     <h1>Hello World</h1>
    </div>
  );
}

export default App;
