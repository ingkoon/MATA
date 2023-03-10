import logo from './logo.svg';
import './App.css';
import {useEffect, useState} from "react";
import {Route, Routes, useLocation} from "react-router-dom";
import FirstVIew from "./view/FirstVIew";
import SecondVIew from "./view/SecondView";
import MainView from "./view/MainView";
import TagManager from "./module";
import * as events from "events";

export default function App() {

  const tagManager = new TagManager('http://localhost:8080/api/v1/dump', "tag-manager-service-token", ['click'])
  const location = useLocation()
  useEffect(() => {
    tagManager.attach();
    return () => {
      tagManager.detach();
    }
  }, [location])

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo tag-manager" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <Routes>
          <Route path='/' element={<MainView />}></Route>
          <Route path='/first' element={<FirstVIew />}></Route>
          <Route path='/second' element={<SecondVIew />}></Route>
        </Routes>
      </header>
    </div>
  );
}
