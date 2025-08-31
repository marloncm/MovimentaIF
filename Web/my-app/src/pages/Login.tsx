import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { auth, googleProvider } from '../firebase'
import { signInWithPopup } from 'firebase/auth'

export default function Login() {
    const navigate = useNavigate()
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)


    const backendUrl = import.meta.env.VITE_BACKEND_URL as string


    const handleGoogle = async () => {
        setError(null)
        setLoading(true)
        try {
            const result = await signInWithPopup(auth, googleProvider)
            const idToken = await result.user.getIdToken()


            // Envia para o backend validar e autorizar (professor/admin)
            const res = await fetch(`${backendUrl}/auth/google`, {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${idToken}`,
                },
            })

            if (!res.ok) {
                const text = await res.text()
                throw new Error(text || 'Acesso negado')
            }


            navigate('/dashboard')
        } catch (e: any) {
            setError(e.message || 'Falha no login')
        } finally {
            setLoading(false)
        }
    }


    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <h1 style={styles.title}>MovimentaIF — Login</h1>
                <button onClick={handleGoogle} disabled={loading} style={styles.googleBtn}>
                    {loading ? 'Entrando…' : 'Entrar com Google'}
                </button>


                {error && <p style={styles.error}>{error}</p>}


                <p style={styles.helper}>
                    Não tem conta (aluno)? <Link to="/register">Cadastre-se</Link>
                </p>
            </div>
        </div>
    )
}

const styles: Record<string, React.CSSProperties> = {
    container: { display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '100vh', background: '#f6f7fb' },
    card: { width: 360, background: '#fff', padding: 24, borderRadius: 16, boxShadow: '0 10px 30px rgba(0,0,0,0.08)', textAlign: 'center' },
    title: { marginBottom: 16 },
    googleBtn: { width: '100%', padding: '12px 16px', borderRadius: 12, border: '1px solid #ddd', cursor: 'pointer', fontWeight: 600 },
    error: { color: '#c0392b', marginTop: 12 },
    helper: { fontSize: 14, marginTop: 16 },
}