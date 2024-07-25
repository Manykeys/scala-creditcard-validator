import requests
import pytest

BASE_URL = 'http://localhost:8080'

def test_valid_card():
    response = requests.get(f'{BASE_URL}/validateCard?cardNumber=4539148803436467')
    assert response.status_code == 200
    assert response.json() == {'valid': True}

def test_invalid_card():
    response = requests.get(f'{BASE_URL}/validateCard?cardNumber=6011111111111117')
    assert response.status_code == 200
    assert response.json() == {'valid': True}

def test_no_card_number():
    response = requests.get(f'{BASE_URL}/validateCard?cardNumber=')
    assert response.status_code == 200
    assert response.json() == {'valid': False}
