import { Outlet } from 'react-router-dom';
import GlobalSpinner from '../GlobalSpinner'; // Ajuste o caminho conforme sua pasta

export default function LayoutGeral() {
  return (
    <>
      <GlobalSpinner />
      <Outlet />
    </>
  );
}