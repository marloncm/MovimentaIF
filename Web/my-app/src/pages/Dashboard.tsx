import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { auth } from '../firebase'
import { onAuthStateChanged, signOut } from 'firebase/auth'


export default function Dashboard() {
    const navigate = useNavigate()
    const [email, setEmail] = useState<string | null>(null)


    useEffect(() => {
        const unsub = onAuthStateChanged(auth, (user) => {
            if (!user) {
                navigate('/login')
            } else {
                setEmail(user.email)
            }
        })
        return () => unsub()
    }, [navigate])


    const handleLogout = async () => {
        await signOut(auth)
        navigate('/login')
    }


    return (
        <div style={{ padding: 24 }}>
            <h1>Dashboard</h1>
            <p>Logado como: <strong>{email}</strong></p>
            <button onClick={handleLogout}>Sair</button>
        </div>
    )
}